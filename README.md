# Pocket IDE

A full development environment for Android.

Write code, run Linux tools, build projects, work with Git, and use AI assistance without leaving your phone.

<p align="center">
  <img src="assets/readme/app_logo.png" alt="Pocket IDE" width="112">
</p>

<p align="center">
  <a href="https://github.com/rahilanw4r/novacode-studio/releases/latest">Download</a>
  ·
  <a href="https://github.com/rahilanw4r/novacode-studio/issues">Issues</a>
  ·
  <a href="https://t.me/RahilAnw4r">Contact</a>
</p>

> Pocket IDE is an Android IDE built around a simple workflow: project → code → run → build → review. AI is integrated into that workflow rather than replacing it.

---

## What Pocket IDE does

Pocket IDE brings the parts of a development workstation that are most useful on a phone into one app:

- Project management and local workspaces
- Code editing and file browsing
- Linux terminal and command execution
- Git and GitHub workflows
- Project templates and toolchains
- Local web development and preview
- Android project builds
- AI-assisted coding, debugging and refactoring
- Background development tasks
- Developer settings and runtime management

The exact features available depend on the project, enabled toolchains, device, and connected AI provider.

## The development loop

```text
Create / Open
      ↓
    Browse
      ↓
     Edit
      ↓
    Run / Test
      ↓
 Build / Debug
      ↓
 Review Changes
      ↓
   Git / Share
      ↺
```

AI can enter at any point in this loop: explain code, implement a change, inspect an error, generate tests, refactor a file, or help investigate a failed build.

## Core capabilities

### Code

Use a mobile-first workspace for editing projects and moving between files without leaving the development environment.

### Terminal

Run shell commands and development tools inside Pocket IDE's Linux environment.

Example:

```bash
pwd
git status
npm install
npm run dev
python3 main.py
```

### Projects

Create projects from supported templates, import existing directories, or work with repositories you already have.

### Git

Work with repositories from the same environment you use to write and run code.

Typical tasks include:

```text
clone → branch → edit → diff → commit → pull / push
```

### AI

Pocket IDE connects AI to the development context instead of keeping it as a separate chat screen.

Depending on the configured provider and runtime, AI can help with:

```text
Ask
Fix
Explain
Build
Refactor
Debug
Review
Test
```

AI requests may use the project context required for the selected task. See [PRIVACY.md](PRIVACY.md) for how data is handled.

### Preview

For supported web projects, run a local development server and open it inside the app for quick testing.

---

## Linux development environment

Pocket IDE includes an unprivileged Linux userspace designed for development on Android.

The runtime is based on PRoot and is stored in the app's private storage. It can provide common developer tools such as:

- Bash and core utilities
- Node.js and npm
- Git
- Python and pip
- C/C++ build tools
- PHP and Composer
- Android development components

Some toolchains are optional and may require additional downloads or storage.

Pocket IDE does not require root access.

## Supported project stacks

| Stack | Examples |
| --- | --- |
| JavaScript / TypeScript | Node.js, Vite and web projects |
| Python | Scripts, FastAPI, Flask and related projects |
| Android | Java/Kotlin projects with the supported Android toolchain |
| C / C++ | Native programs, command-line tools and CMake projects |
| PHP | PHP applications and Composer-based projects |
| Shell | Bash scripts and Linux automation |

Support can vary by project configuration and runtime version.

---

## AI providers

Pocket IDE is designed to work with configurable AI backends rather than locking the editor to one model.

Provider availability and authentication methods can change independently of the IDE. Configure providers from the app and use the model supported by that integration.

Do not put API keys in source files, commits, screenshots, or issue reports.

---

## Security and privacy

Pocket IDE is local-first, but "local-first" does not mean every operation stays on the device.

Your projects, runtime data, terminal history and other app-managed information are stored in the app's private Android storage. Provider credentials are protected using Android Keystore-backed encryption.

When you use an external AI provider, the information required for that request can leave the device. Depending on the operation, this may include prompts, relevant source code, file contents, tool output, or attachments.

For the full data-handling policy, see [PRIVACY.md](PRIVACY.md).

## Requirements

- Android 9.0 / API 28 or newer
- ARM64 device
- At least 3 GB RAM recommended
- Additional storage for optional runtimes and Android toolchains
- No root required

Performance depends heavily on device hardware, available RAM, storage speed, and the size of the project or toolchain.

---

## Install

Download the latest Android build from:

**[GitHub Releases](https://github.com/rahilanw4r/novacode-studio/releases/latest)**

For source builds, clone the repository and use the included Gradle wrapper:

```bash
git clone https://github.com/rahilanw4r/novacode-studio.git
cd novacode-studio
./gradlew :app:assembleOnlineDebug
```

Check the repository's current build scripts and workflows for the supported release variants.

## Building from source

Pocket IDE uses:

- Kotlin
- Jetpack Compose
- Android Gradle Plugin
- CMake / native components
- A bundled or downloaded Linux runtime
- Git submodules for selected third-party components

Some release builds include runtime assets that are intentionally kept outside the normal source compilation path.

For contributor-facing details, start with the repository's `docs/`, `scripts/`, and `.github/workflows/` directories.

---

## Project structure

The repository is organised roughly like this:

```text
.
├── app/                  Android application
├── assets/               Project and README assets
├── docs/                 Project documentation
├── dist/                 Runtime bundle outputs
├── fastlane/             Release metadata
├── fdroid/               F-Droid metadata
├── scripts/              Build and runtime scripts
├── third_party/          Third-party components / submodules
├── PRIVACY.md            Privacy policy
└── README.md             Project overview
```

## Development notes

Pocket IDE contains native runtime components and Android-specific integrations, so repository changes can affect more than the UI layer.

Before changing runtime, authentication, storage, networking, or build code:

1. Check the relevant existing implementation.
2. Run the appropriate tests.
3. Verify online and offline variants when the change affects runtime delivery.
4. Avoid committing secrets or generated release credentials.

---

## Roadmap

The roadmap is intentionally kept close to the actual product.

Planned work may include improvements to:

- Editing and navigation on small screens
- Runtime setup and repair
- Project import/export
- Git workflows and change review
- AI-assisted development flows
- Preview and developer tooling
- Performance and reliability

Roadmap items are subject to implementation and device compatibility.

---

## Contributing

Bug reports, feature requests, testing feedback and code contributions are welcome.

Before opening an issue:

- Check existing issues.
- Include the Pocket IDE version.
- Include Android version and device architecture when relevant.
- Provide reproducible steps for bugs.
- Remove API keys, private source code and personal tokens from logs.

For larger changes, open an issue first so the intended direction can be discussed.

## License

The repository currently contains a license file; verify the repository metadata and `LICENSE` before redistributing or changing licensing terms.

## Credits

Created and maintained by **Rahil Anwar**.

- GitHub: [@rahilanw4r](https://github.com/rahilanw4r)
- Telegram: [@RahilAnw4r](https://t.me/RahilAnw4r)

---

<p align="center">
  <sub>Pocket IDE · Development on Android</sub>
</p>
