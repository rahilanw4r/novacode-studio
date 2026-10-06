package com.pocketide.app.runtime

import java.io.File
import java.net.HttpURLConnection
import java.net.URL
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder

class LocalStaticServerTest {

    @get:Rule
    val tempFolder = TemporaryFolder()

    @Test
    fun `detects static html project`() {
        val root = tempFolder.newFolder("static_site")
        File(root, "index.html").writeText("<!DOCTYPE html><html><body><h1>Hello</h1></body></html>")
        File(root, "style.css").writeText("body { background: black; }")

        val kind = ProjectWebDetector.detect(root)
        assertEquals(ProjectWebKind.STATIC_HTML, kind)
    }

    @Test
    fun `detects vite project from package json`() {
        val root = tempFolder.newFolder("vite_app")
        File(root, "package.json").writeText(
            """
            {
              "name": "vite-project",
              "scripts": { "dev": "vite" },
              "devDependencies": { "vite": "^5.0.0" }
            }
            """.trimIndent()
        )

        val kind = ProjectWebDetector.detect(root)
        assertEquals(ProjectWebKind.VITE, kind)
    }

    @Test
    fun `detects next js project from package json`() {
        val root = tempFolder.newFolder("next_app")
        File(root, "package.json").writeText(
            """
            {
              "name": "next-project",
              "scripts": { "dev": "next dev" },
              "dependencies": { "next": "14.0.0" }
            }
            """.trimIndent()
        )

        val kind = ProjectWebDetector.detect(root)
        assertEquals(ProjectWebKind.NEXT_JS, kind)
    }

    @Test
    fun `serves static files over loopback http`() {
        val root = tempFolder.newFolder("web_root")
        File(root, "index.html").writeText("<!DOCTYPE html><html><body><h1>Pocket Preview</h1></body></html>")
        File(root, "app.js").writeText("console.log('loaded');")

        val server = LocalStaticServer(preferredPort = 0)
        val port = server.startServing(root)
        assertTrue(port > 0)
        assertTrue(server.isServerRunning)

        try {
            // Check health
            val reachable = runBlocking { PreviewHealthChecker.isReachable("127.0.0.1", port) }
            assertTrue(reachable)

            // Request index.html
            val url = URL("http://127.0.0.1:$port/")
            val connection = url.openConnection() as HttpURLConnection
            connection.connectTimeout = 3000
            connection.readTimeout = 3000
            connection.requestMethod = "GET"

            val responseCode = connection.responseCode
            assertEquals(200, responseCode)
            val body = connection.inputStream.bufferedReader().readText()
            assertTrue(body.contains("Pocket Preview"))
            assertEquals("text/html; charset=utf-8", connection.contentType)
            connection.disconnect()

            // Request app.js
            val jsUrl = URL("http://127.0.0.1:$port/app.js")
            val jsConn = jsUrl.openConnection() as HttpURLConnection
            assertEquals(200, jsConn.responseCode)
            val jsBody = jsConn.inputStream.bufferedReader().readText()
            assertTrue(jsBody.contains("loaded"))
            assertEquals("application/javascript; charset=utf-8", jsConn.contentType)
            jsConn.disconnect()
        } finally {
            server.stop()
            assertFalse(server.isServerRunning)
        }
    }

    @Test
    fun `detects node project from server js file without package json`() {
        val root = tempFolder.newFolder("node_script_site")
        File(root, "server.js").writeText("const express = require('express'); app.listen(3000);")
        File(root, "index.html").writeText("<html><body>App</body></html>")

        val kind = ProjectWebDetector.detect(root)
        assertEquals(ProjectWebKind.NODE, kind)
        assertTrue(ProjectWebDetector.hasStaticHtml(root))
        assertEquals(3000, ProjectWebDetector.detectCustomPort(root))
        assertEquals("node server.js", ProjectWebDetector.resolveDevCommand(root))
    }

    @Test
    fun `detects custom port in python and node files`() {
        val root = tempFolder.newFolder("custom_port_site")
        File(root, "app.js").writeText("const PORT = 5000; server.listen(PORT);")

        assertEquals(5000, ProjectWebDetector.detectCustomPort(root))
        assertEquals("node app.js", ProjectWebDetector.resolveDevCommand(root))
    }

    @Test
    fun `checks reachable ports with PreviewHealthChecker`() {
        val root = tempFolder.newFolder("static_root")
        File(root, "index.html").writeText("<h1>Static</h1>")
        val server = LocalStaticServer(preferredPort = 0)
        val port = server.startServing(root)

        try {
            val ports = listOf(port, 9998, 9999)
            val reachable = runBlocking { PreviewHealthChecker.checkPorts(ports = ports) }
            assertTrue(reachable.contains(port))
            assertFalse(reachable.contains(9999))

            val first = runBlocking { PreviewHealthChecker.findFirstReachablePort(ports = ports) }
            assertEquals(port, first)
        } finally {
            server.stop()
        }
    }
}
