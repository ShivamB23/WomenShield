<div align="center">

<img src="https://raw.githubusercontent.com/ABSphreak/ABSphreak/master/gifs/Hi.gif" width="60px" />

# 🛡️ SHEild: Intelligent Women Safety & IoT Tracker 🛡️

### *Automated Distress Alerts | Hardware IoT Syncing | Hidden Camera Detector | Therapy Lounge*

[![Typing SVG](https://readme-typing-svg.demolab.com?font=Fira+Code&size=20&duration=3000&pause=1000&color=E5383B&center=true&vCenter=true&width=450&height=40&lines=Real-Time+GPS+Tracking;IoT+Hardware+Integration;One-Touch+Emergency+SOS;Hidden+Camera+Scanner;Therapy+%26+Mental+Support)](https://git.io/typing-svg)

<div style="margin: 15px 0;">
  <img src="https://img.shields.io/badge/Platform-Android-3DDC84?style=for-the-badge&logo=android&logoColor=white" alt="Android" />
  <img src="https://img.shields.io/badge/Language-Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white" alt="Java" />
  <img src="https://img.shields.io/badge/Build-Gradle-02303A?style=for-the-badge&logo=gradle&logoColor=white" alt="Gradle" />
  <img src="https://img.shields.io/badge/Location-Google--Services-4285F4?style=for-the-badge&logo=google&logoColor=white" alt="Google Services" />
</div>

---

**SHEild** is a premium Android application designed to secure and empower women. By integrating mobile geolocation APIs, hardware IoT interfaces, local privacy tools, and mental wellness resources, SHEild creates an active safety net for users in real-world scenarios.

</div>

---

## ⚡ Main Capabilities

| Feature | Tech Stack / Components | Core Action |
| :--- | :--- | :--- |
| 🛰️ **IoT GPS Tracker** | `FusedLocationProviderClient` | Fetches high-accuracy device location coordinates rather than hardcoded mock data. |
| 🚨 **Emergency SOS** | `SmsManager`, `Intent.ACTION_CALL` | Dispatches multi-recipient SMS distress messages with Google Maps links and auto-dials emergency contacts. |
| 🔊 **Siren Alert** | `MediaPlayer` | Sounds a high-volume police siren loop to warn bystanders and deter active threats. |
| 📷 **Cam Finder** | Device Sensors | Identifies potential hidden cameras in changing rooms and private spaces. |
| 🧘 **Therapy Section** | Audio/Video modules | Access to audio therapy, guided yoga videos, and self-help book libraries. |

---

## 🛠️ Interactive Feature Deep-Dive

<details>
<summary><b>🛰️ Real-Time IoT GPS Syncing</b></summary>
<br />
SHEild queries Google Play Services Location APIs to pull real-time location. The app requests location permissions and updates current coordinates dynamically.
<pre>
fusedLocationProviderClient.getCurrentLocation(
    Priority.PRIORITY_HIGH_ACCURACY, 
    tokenSource.getToken()
).addOnCompleteListener(task -> {
    // Real coordinates mapped automatically
});
</pre>
</details>

<details>
<summary><b>🚨 Triple-Alert SOS Dispatch</b></summary>
<br />
Triggering the primary SOS button triggers:
1. **Loud Alarm**: Fires a continuous audio loop of a police siren.
2. **Distress SMS**: Broadcasts a customizable warning message with coordinates to up to 4 trusted contacts.
3. **Emergency Call**: Instantly dials the main contact number.
</details>

<details>
<summary><b>🧘 Integrated Therapy Lounge</b></summary>
<br />
Designed for decompression and mental wellness:
- **Audio Therapy**: Relaxing music and ambient loops.
- **Yoga Routines**: Visual tutorials and home guides.
- **Self-Help Hub**: Offline reading materials and self-paced guides.
</details>

---

## 🚀 Setup & Installation

1. **Clone the Repository**:
   ```bash
   git clone https://github.com/ShivamB23/WomenShield.git
   ```
2. **Open in Android Studio**:
   - Let Gradle sync build resources and resolve dependencies.
3. **Configure API Credentials**:
   - Place your Google Services configuration and API keys inside `local.properties`.
4. **Build & Run**:
   - Run the `:app` configurations on a physical device or emulator.

---

## 👨‍💻 Developed By

<div align="center">

### **Hey there 👋, I'm Aditya!**

[![Linkedin Badge](https://img.shields.io/badge/-LinkedIn-0e76a8?style=flat-square&logo=Linkedin&logoColor=white)](https://www.linkedin.com/in/aditya-adi-konda/)
[![Twitter Badge](https://img.shields.io/badge/-Twitter-00acee?style=flat-square&logo=Twitter&logoColor=white)](https://twitter.com/AdityaKonda7)
[![Instagram Badge](https://img.shields.io/badge/-Instagram-e4405f?style=flat-square&logo=Instagram&logoColor=white)](https://www.instagram.com/konda_aditya/)

<img align="right" height="200" width="300" style="border-radius: 8px;" alt="Coding" src="https://raw.githubusercontent.com/iampavangandhi/iampavangandhi/master/gifs/coder.gif" />

I’m a **2025 IT Graduate** passionate about **DevOps, Cloud, and Software Development** 🚀.  
I love bridging the gap between development and operations—automating CI/CD workflows and deploying secure, scalable applications.

</div>

### 💼 Technical Toolkit

```
Primary:     Java | SQL | Linux | Git | Python
Frameworks:  Maven | Selenium Automation | Android Studio SDK
DevOps:      Docker | Kubernetes | AWS Cloud | Jenkins CI-CD
```

---

### 📊 Real-Time GitHub Performance

<div align="center">

<details open>
  <summary><b>⚡ Developer Stats</b></summary>
  <br />
  <img height="180em" src="https://github-readme-stats.vercel.app/api?username=adityakonda6&show_icons=true&hide_border=true&count_private=true&include_all_commits=true&theme=tokyonight" alt="Aditya's GitHub Stats" />
  <img height="180em" src="https://github-readme-stats.vercel.app/api/top-langs/?username=adityakonda6&show_icons=true&hide_border=true&layout=compact&langs_count=8&theme=tokyonight" alt="Top Languages" />
</details>

<br />

<details open>
  <summary><b>🔥 Activity Streaks</b></summary>
  <br />
  <img height="180em" src="https://github-readme-streak-stats.herokuapp.com/?user=adityakonda6&hide_border=true&theme=tokyonight" alt="GitHub Streaks" />
</details>

<br />

<details>
  <summary><b>☄️ LeetCode Rating</b></summary>
  <br />
  <img src="https://leetcard.jacoblin.cool/adityakonda04?theme=tokyonight&font=Coda%20Caption&ext=heatmap" alt="LeetCode Dashboard" />
</details>

</div>

---

<div align="center">

💬 Always open to collaborations, DevOps projects, and automation challenges. Feel free to reach out via **adityakonda04@gmail.com** or check out my [Portfolio Website](https://adityakonda04.vercel.app/).

⭐️ *Show some support by starring this repository if you find it helpful!* ⭐️

</div>
