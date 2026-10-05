<div align="center">

  <img src="assets/readme/app_logo.png" alt="NovaCode Studio Official Logo" width="128" height="128" style="border-radius: 28px; box-shadow: 0 8px 24px rgba(0,0,0,0.4);" />

  # ⚡ NovaCode Studio

  ### Mobile-First Linux IDE & Autonomous AI Coding Environment for Android
  **Turn your Android smartphone into a complete, standalone software engineering workstation.**

  <br />

  [![Android 9.0+](https://img.shields.io/badge/Android-9.0%2B-10B981?style=for-the-badge&logo=android&logoColor=white)](#-system-requirements)
  [![Architecture ARM64](https://img.shields.io/badge/Architecture-ARM64--v8a-6366F1?style=for-the-badge&logo=arm&logoColor=white)](#-system-requirements)
  [![Latest Release](https://img.shields.io/badge/Release-v1.0.15-06B6D4?style=for-the-badge&logo=github&logoColor=white)](https://github.com/rahilanw4r/novacode-studio/releases/latest)
  [![Build Status](https://img.shields.io/badge/CI%2FCD-Passing-10B981?style=for-the-badge&logo=githubactions&logoColor=white)](https://github.com/rahilanw4r/novacode-studio/actions)
  [![Telegram Contact](https://img.shields.io/badge/Telegram-@RahilAnw4r-229ED9?style=for-the-badge&logo=telegram&logoColor=white)](https://t.me/RahilAnw4r)
  [![License Apache 2.0](https://img.shields.io/badge/License-Apache_2.0-F59E0B?style=for-the-badge)](LICENSE)

  <br />

  [**📥 Download Latest APK (v1.0.15)**](https://github.com/rahilanw4r/novacode-studio/releases/latest) • [**💬 Telegram Contact**](https://t.me/RahilAnw4r) • [**⭐ Star on GitHub**](https://github.com/rahilanw4r/novacode-studio) • [**🐛 Report Issue**](https://github.com/rahilanw4r/novacode-studio/issues)

  <br />

  <p align="center">
    <em>Engineered & Developed by <strong><a href="https://github.com/rahilanw4r">Rahil Anwar</a></strong> (Telegram: <a href="https://t.me/RahilAnw4r">@RahilAnw4r</a>)</em>
  </p>

</div>

---

## 📌 Table of Contents

- [Overview](#-overview)
- [Why NovaCode Studio?](#-why-novacode-studio)
- [Key Features](#-key-features)
- [Screenshots & Walkthrough](#-screenshots--walkthrough)
- [Navigation Architecture](#-navigation-architecture)
- [Supported Language Toolchains](#-supported-language-toolchains)
- [Privacy, Security & Credential Storage](#-privacy-security--credential-storage)
  - [Are Your Keys & Credentials Safe?](#-are-your-keys--credentials-safe)
  - [Hardware-Backed AES-256-GCM Encryption](#1-hardware-backed-aes-256-gcm-key-storage)
  - [Google AntiGravity Authentication](#2-google-antigravity-connection--oauth-security)
  - [GitHub CLI Integration](#3-github-connect-official-oauth-device-flow)
  - [Zero Intermediary Proxies or Cloud Telemetry](#4-zero-intermediary-proxies-or-cloud-telemetry)
  - [Isolated Android PRoot Sandbox](#5-isolated-android-proot-sandbox)
  - [Full Offline Capability](#6-full-offline-capability)
- [System Requirements](#-system-requirements)
- [Installation & Getting Started](#-installation--getting-started)
- [Building From Source](#-building-from-source)
- [Author & Credits](#-author--credits)
- [License](#-license)

---

## 📖 Overview

**NovaCode Studio** is an open-source, mobile-first Linux development environment and autonomous AI workspace designed specifically for Android devices. Unlike cloud-streamed containers or simple mobile code editors, NovaCode Studio operates **100% locally on your phone** using an isolated **PRoot Linux userspace (Ubuntu ARM64)**.

Compile real programs, run local development servers, launch multi-tab Linux terminals, inspect side-by-side Git diffs, and collaborate with autonomous AI coding agents — including **Google AntiGravity (Gemini 3.8)**, **Claude Code (3.7 Sonnet)**, and **DeepSeek Coder** — with **zero root access** and **no PC tethering required**.

---

## 💡 Why NovaCode Studio?

| Traditional Mobile Code Editors | Cloud Remote Workspaces (Codespaces/Gitpod) | ⚡ NovaCode Studio |
| :--- | :--- | :--- |
| ❌ Syntax highlighting only; cannot compile or run code | ⚠️ Requires constant high-speed internet & subscription | ✅ **Runs real compilers & runtimes directly on your phone** |
| ❌ No terminal or standard Linux tools (`npm`, `pip`, `make`) | ⚠️ Metered cloud compute hours & vendor lock-in | ✅ **Full Ubuntu PRoot subsystem with Git, Node, Python, and C/C++** |
| ❌ No autonomous AI agent terminal execution | ❌ Your source code lives on third-party remote servers | ✅ **Direct AI agent pair-programming right in your local filesystem** |
| ❌ Cluttered desktop UI crammed into mobile screens | ⚠️ High latency, poor touch ergonomics on phone keyboards | ✅ **Bespoke Apple Liquid AI UI with floating developer key toolbar** |

---

## ✨ Key Features

<table>
  <tr>
    <td width="50%" valign="top">
      <h3>🤖 Autonomous AI Coding Agents</h3>
      <ul>
        <li><strong>Google AntiGravity</strong>: Native integration with Google DeepMind's CLI agent with seamless browser OAuth login and full Gemini 3.8 / 3.6 / 3.1 reasoning models.</li>
        <li><strong>Claude Code</strong>: Anthropic's autonomous software engineering CLI with tool-use, multi-file editing, and streaming reasoning chains.</li>
        <li><strong>DeepSeek Coder & OpenAI</strong>: Support for DeepSeek Coder CLI, GPT-4o, and o3-mini via direct API keys or local OpenRouter routing.</li>
      </ul>
    </td>
    <td width="50%" valign="top">
      <h3>🐧 Isolated Linux Runtime (PRoot)</h3>
      <ul>
        <li>Full <strong>Ubuntu ARM64</strong> userspace running safely inside Android app storage without requiring root.</li>
        <li>Pre-installed with <strong>Node.js LTS</strong>, <strong>npm</strong>, and <strong>Git</strong>.</li>
        <li>On-demand 1-click toolchains for <strong>Python</strong> (pip, venv), <strong>C/C++</strong> (gcc, g++, cmake), <strong>PHP</strong> (Composer), and native <strong>Android Compilation</strong> (SDK 36, Gradle 8.14.3, AAPT2).</li>
      </ul>
    </td>
  </tr>
  <tr>
    <td width="50%" valign="top">
      <h3>🎨 Apple-Inspired Liquid AI Design</h3>
      <ul>
        <li>Modern developer-tool interface built natively with <strong>Jetpack Compose</strong>.</li>
        <li>Refined <strong>Obsidian Dark</strong> and dynamic <strong>Light Palette</strong> modes with emerald green accents (<code>#18C78A</code>).</li>
        <li>Subtle glassmorphic cards, crisp typography, 48dp+ touch targets, and fluid gesture navigation.</li>
      </ul>
    </td>
    <td width="50%" valign="top">
      <h3>🔍 Granular Git Diff Inspector</h3>
      <ul>
        <li>Review file modifications proposed by AI agents before applying them.</li>
        <li>Syntax-highlighted unified and split diff views with individual <strong>Accept</strong> and <strong>Revert</strong> buttons per hunk.</li>
        <li>Integrated GitHub CLI (<code>gh</code>) for signing in, cloning private repositories, and managing pull requests.</li>
      </ul>
    </td>
  </tr>
  <tr>
    <td width="50%" valign="top">
      <h3>⌨️ Floating Developer Key Toolbar</h3>
      <ul>
        <li>Ergonomic row hovering directly above the on-screen keyboard.</li>
        <li>Instant single-tap access to critical developer keys: <code>ESC</code>, <code>TAB</code>, <code>CTRL</code>, <code>ALT</code>, <code>|</code>, <code>~</code>, <code>/</code>, <code>-</code>, <code>_</code>, and arrow navigation.</li>
        <li>Customizable snippet expansion and terminal shortcuts.</li>
      </ul>
    </td>
    <td width="50%" valign="top">
      <h3>🌐 Multi-Device Web DevTools Preview</h3>
      <ul>
        <li>Built-in WebView browser emulator connected to local development servers (Vite, Next.js, FastAPI, Flask, etc.).</li>
        <li>One-tap responsive viewport switching (Mobile 375px, Tablet 768px, Desktop Full).</li>
        <li>Live console log viewer capturing JavaScript logs, warnings, and errors in real time.</li>
      </ul>
    </td>
  </tr>
</table>

---

## 📸 Screenshots & Walkthrough

<div align="center">
  <table>
    <tr>
      <td align="center" width="33%">
        <img src="assets/readme/setup-provider.png" alt="AI Agent Setup" width="100%" />
        <br /><strong>1. AI Agent Selection</strong>
      </td>
      <td align="center" width="33%">
        <img src="assets/readme/setup-toolchains.png" alt="Development Toolchains" width="100%" />
        <br /><strong>2. Language Toolchains</strong>
      </td>
      <td align="center" width="33%">
        <img src="assets/readme/projects.png" alt="Project Workspace" width="100%" />
        <br /><strong>3. Projects & Templates</strong>
      </td>
    </tr>
    <tr>
      <td align="center" width="33%">
        <img src="assets/readme/terminal.png" alt="Linux Terminal" width="100%" />
        <br /><strong>4. Multi-Tab Terminal</strong>
      </td>
      <td align="center" width="33%">
        <img src="assets/readme/settings.png" alt="Preferences & Themes" width="100%" />
        <br /><strong>5. Preferences & Themes</strong>
      </td>
      <td align="center" width="33%">
        <img src="assets/readme/setup-notifications.png" alt="Background Service" width="100%" />
        <br /><strong>6. Background Build Service</strong>
      </td>
    </tr>
  </table>
</div>

---

## 🗂️ Navigation Architecture

NovaCode Studio organizes the development workflow into 5 streamlined destinations:

```
┌─────────────────────────────────────────────────────────────────┐
│                          NovaCode Studio                        │
├───────────┬──────────────┬─────────────┬─────────────┬──────────┤
│   Home    │   Projects   │   Copilot   │  Terminal   │   More   │
└───────────┴──────────────┴─────────────┴─────────────┴──────────┘
```

1. **Home**: Fast actions, active sandbox status, recent project shortcuts, and one-tap starter templates (React + Vite, FastAPI, Node.js, Python, Android).
2. **Projects**: Comprehensive file tree, code editor with syntax highlighting, live preview panel, and hunk-by-hunk Git diff inspector.
3. **Copilot**: Interactive AI pair programming chat, quick command chips (*Fix Code*, *Explain*, *Build*, *Refactor*), model selector, and execution logs.
4. **Terminal**: Hardware-accelerated multi-session terminal emulator with ANSI color support, custom shell profiles, and dev toolbar.
5. **More**: Extended tools and utilities:
   - ⚙️ **Settings**: Appearance (Dark/Light), editor fonts, tab spacing, and keybindings.
   - 🛠️ **Developer Tools**: Memory stats, PRoot virtualization inspection, and debug bridges.
   - 🐧 **Linux Runtime**: Manage installed stacks, purge caches, or repair packages.
   - 🔄 **Update Channel**: One-tap in-app updates for AI agent binaries and studio releases.
   - 🐙 **Git & GitHub**: Authentication status, SSH keys, and remote repository manager.
   - 👤 **About Creator**: Developer profile, Telegram contact, and release details.

---

## 🧰 Supported Language Toolchains

| Stack | Included Tools | Supported Project Types | Verify Command |
| :--- | :--- | :--- | :--- |
| **Core (Always On)** | Node.js LTS, npm, Git, bash, coreutils | JavaScript, TypeScript, Shell scripts, Git repos | `node -v && git --version` |
| **Python** | Python 3, pip, venv, build tools | FastAPI, Flask, Django, automation, data science | `python3 --version && pip3 --version` |
| **Web Dev** | Node.js, Vite, npm, npx | React, Vue, Svelte, Next.js, HTML/CSS/JS | `npm --version` |
| **Android** | OpenJDK 17, Android SDK 36, AAPT2, Gradle 8.14.3, Offline Maven | Native Android Java & Kotlin applications | `java -version && gradle --version` |
| **C / C++** | gcc, g++, make, cmake, gdb | C/C++ compiled binaries, algorithms, systems | `gcc --version && cmake --version` |
| **PHP** | php-cli, common extensions, Composer | Classic PHP sites, Laravel, Composer packages | `php --version && composer --version` |

---

## 🔒 Privacy, Security & Credential Storage

Your privacy and source code security are central to NovaCode Studio's architecture. Here is a transparent breakdown of how credentials, connections, and files are protected on your device.

### 🛡️ Are Your Keys & Credentials Safe?

> **Yes, 100%.** NovaCode Studio never transmits your code, API keys, or personal tokens to any developer-owned server or third-party proxy. Everything is stored locally on your device and protected by Android's hardware security layer.

---

### 1. Hardware-Backed AES-256-GCM Key Storage
When you enter an API key for Anthropic Claude, OpenAI, DeepSeek, or Google Gemini:
- **No Plaintext on Disk**: Keys are never stored in unencrypted plain text files or standard shared preferences.
- **AndroidKeyStore Integration**: Keys are encrypted using `AES/GCM/NoPadding` with a 256-bit cryptographic key generated inside the **AndroidKeyStore** hardware security module (`TEE` or `StrongBox`).
- **Unique Initialization Vectors**: Every secret is encrypted with a cryptographically random initialization vector (`IV`) and authentication tag (`GCMParameterSpec`), preventing tampering and replay attacks.
- **App Isolation**: Android's Linux kernel enforces strict application sandbox isolation. No other application installed on your device can access or decrypt your keys.

---

### 2. Google AntiGravity Connection & OAuth Security
When signing into **Google AntiGravity**:
- **Official Google OAuth 2.0 PKCE**: Authentication uses standard Google OAuth with RFC 7636 **PKCE (Proof Key for Code Exchange)**.
- **Direct Google Exchange**: You log in securely using your phone's default web browser at `https://accounts.google.com`. NovaCode Studio never sees or handles your Google password.
- **Local Token Storage**: The resulting authentication token is exchanged directly with Google and stored exclusively inside the private Linux rootfs directory (`/root/.gemini/antigravity-cli/antigravity-oauth-token`) within the app's internal sandbox.

---

### 3. GitHub Connect (Official OAuth Device Flow)
When connecting your GitHub account to browse and clone repositories:
- **Official GitHub CLI (`gh`)**: NovaCode Studio uses GitHub's official command-line tool.
- **Device Code Flow**: You authenticate directly on `https://github.com/login/device` by confirming a one-time verification code in your browser.
- **No Passwords Stored**: NovaCode Studio never asks for or stores your GitHub username or password. The scoped OAuth token is managed directly by the official GitHub CLI within the private PRoot sandbox.

---

### 4. Zero Intermediary Proxies or Cloud Telemetry
- **Direct API Communication**: Every request sent to an AI model travels directly from your phone's network connection to the official provider endpoint (e.g., `api.anthropic.com`, `generativelanguage.googleapis.com`, `api.openai.com`, or `api.deepseek.com`).
- **No Middleman Servers**: There are no proxy servers, relay gateways, or analytics aggregators between your phone and your AI provider.
- **No Telemetry or Tracking**: NovaCode Studio contains no analytics trackers, tracking beacons, advertising SDKs, or data harvesting code.

---

### 5. Isolated Android PRoot Sandbox
- The Linux root filesystem (`Ubuntu ARM64`) lives entirely within Android's private app data storage:
  ```
  /data/data/com.novacode.studio/files/runtime/ubuntu/
  ```
- Android's security architecture assigns each app a unique Linux UID. Other apps cannot read your projects, source code, Git repositories, or Linux home directory.
- All code compilation and script execution occurs inside an unprivileged PRoot user namespace, eliminating any risk to your Android system partition.

---

### 6. Full Offline Capability
- You do **not** need an internet connection to use NovaCode Studio.
- The Linux shell, Git version control, Python interpreter, C/C++ compilers, Node.js runtime, and Android toolchain function **100% offline**.
- You only need internet access when executing online AI model prompts or running `git push` / `git pull` to remote repositories.

---

## 📱 System Requirements

- **Operating System**: Android 9.0 (API Level 28) or higher
- **Architecture**: **ARM64** (`aarch64` / `arm64-v8a`)
- **Memory**: 3 GB RAM or more recommended
- **Storage Space**:
  - ~300 MB for Base Core Runtime (Node.js + Git + Ubuntu base)
  - ~1.5 GB recommended if enabling Python, C/C++, and Android toolchains
- **Root Permissions**: **Zero Root Required** (runs completely in unprivileged user space)

---

## 🚀 Installation & Getting Started

### Method 1: Download Prebuilt APK (Recommended)
1. Download the latest release from the **[GitHub Releases Page](https://github.com/rahilanw4r/novacode-studio/releases/latest)** (`NovaCode-Studio-v1.0.15.apk`).
2. Open the downloaded file on your Android device and confirm installation.
3. Grant notification permissions when prompted to allow background build and terminal monitoring.
4. Follow the interactive onboarding setup to initialize the private Linux runtime and pick your preferred AI agent.

### Method 2: Build From Source

```bash
# 1. Clone the repository
git clone https://github.com/rahilanw4r/novacode-studio.git
cd novacode-studio

# 2. Compile native PRoot bindings and Android APK
./gradlew :app:assembleOnlineDebug

# 3. Locate generated APK
ls -lh app/build/outputs/apk/online/debug/NovaCode-Studio-*.apk
```

---

## 👨‍💻 Author & Credits

**NovaCode Studio** is designed, engineered, and maintained by:

<div align="left">
  <table>
    <tr>
      <td><strong>Creator & Lead Engineer</strong></td>
      <td><strong>Rahil Anwar</strong></td>
    </tr>
    <tr>
      <td><strong>GitHub</strong></td>
      <td><a href="https://github.com/rahilanw4r">@rahilanw4r</a></td>
    </tr>
    <tr>
      <td><strong>Telegram Contact</strong></td>
      <td><a href="https://t.me/RahilAnw4r">@RahilAnw4r</a></td>
    </tr>
    <tr>
      <td><strong>Repository</strong></td>
      <td><a href="https://github.com/rahilanw4r/novacode-studio">rahilanw4r/novacode-studio</a></td>
    </tr>
  </table>
</div>

> *"Bringing desktop-grade software engineering tools to the device you carry everywhere."*

---

## 📄 License

This project is licensed under the **Apache License 2.0**. See the [LICENSE](LICENSE) file for complete details.
