# Home Workout

Home Workout adalah aplikasi kebugaran Android modern yang dibangun dengan Kotlin dan Jetpack Compose. Aplikasi ini dirancang untuk membantu pengguna tetap konsisten dengan latihan di rumah menggunakan bodyweight workout, melacak progres, dan mengelola rutinitas kesehatan sederhana tanpa perlu ke gym atau koneksi internet.

Aplikasi ini mencakup rutinitas latihan, pembuatan workout kustom, panduan latihan, pelacakan progres, serta dashboard ringkas untuk makanan dan laporan. Struktur aplikasinya mengikuti pendekatan local-first dengan penyimpanan lokal menggunakan Room dan antarmuka berbasis Compose.

## Fitur

- Rencana latihan bodyweight
- Pemutaran latihan dan timer
- Fase countdown, kerja, dan istirahat
- Kontrol pause/resume
- Pembuatan workout kustom
- Halaman detail workout dan informasi latihan
- Pelacakan progres dan riwayat penyelesaian workout
- Data meal plan offline
- Pelacak asupan air
- Notifikasi suara dan efek suara selama latihan
- Layar pengaturan untuk preferensi workout
- Navigasi berbasis tab: Training, Meals, Report, Settings

## Teknologi yang Digunakan

- Kotlin
- Jetpack Compose
- Android Activity + Lifecycle
- Room Database
- ViewModel + StateFlow
- Navigation Compose
- Coroutines
- Retrofit + Moshi + OkHttp
- Firebase AI / Firebase App Check
- Gradle Kotlin DSL

## Struktur Proyek

```text
my-home-workout/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/example/
│   │   │   │   ├── MainActivity.kt
│   │   │   │   ├── data/
│   │   │   │   │   ├── local/
│   │   │   │   │   ├── model/
│   │   │   │   │   └── repository/
│   │   │   │   └── ui/
│   │   │   │       ├── components/
│   │   │   │       ├── screens/
│   │   │   │       ├── theme/
│   │   │   │       └── WorkoutViewModel.kt
│   │   │   └── res/
│   │   ├── androidTest/
│   │   └── test/
│   ├── build.gradle.kts
│   └── proguard-rules.pro
├── build.gradle.kts
├── settings.gradle.kts
├── gradle.properties
├── gradle/
│   ├── wrapper/
│   └── libs.versions.toml
├── .env.example
├── metadata.json
├── .gitignore
└── gradle.properties
```

## Arsitektur Aplikasi

Proyek ini mengikuti arsitektur sederhana berlapis:

- Layer UI: Jetpack Compose screens
- Layer state: WorkoutViewModel
- Layer data: Room DAO + repository
- Model domain: workout entities, user progress entities, custom workout entities

Aplikasi menyimpan data latihan secara lokal melalui Room dan menyalurkan state menggunakan Flow di dalam ViewModel.

## Prasyarat

Sebelum menjalankan proyek, pastikan Anda sudah memiliki:

- Android Studio
- JDK 17 atau lebih baru
- SDK Android yang sudah dikonfigurasi
- Gradle wrapper tersedia
- Proyek Firebase jika ingin mengaktifkan fitur Firebase

## Setup

1. Clone repository
2. Buka proyek di Android Studio
3. Izinkan Gradle sync
4. Konfigurasi variabel lingkungan jika diperlukan
5. Jalankan aplikasi di emulator atau perangkat Android

## Konfigurasi Lingkungan

Proyek ini menggunakan pola setup berbasis .env melalui Secrets Gradle Plugin. Repository sudah mencakup:

- .env.example

Anda dapat menyalin file tersebut dan membuat file .env Anda sendiri sesuai kebutuhan konfigurasi Firebase atau environment yang akan digunakan.

## Build dan Jalankan

```bash
./gradlew assembleDebug
```

atau jalankan langsung dari Android Studio melalui konfigurasi aplikasi default.

## Catatan

- Aplikasi ini dirancang sebagai alat kebugaran offline-first.
- Dependensi Firebase sudah ada dalam proyek, tetapi beberapa fitur Firebase bisa jadi bersifat opsional tergantung konfigurasi yang digunakan.
- Proyek ini tampak seperti fondasi yang kuat untuk aplikasi home workout dengan UI Compose yang kaya dan model penyimpanan lokal yang baik.

## Lisensi

Proyek ini belum menampilkan file lisensi yang jelas di repository. Jika Anda berencana mendistribusikan atau mempublikasikan proyek, tambahkan lisensi yang sesuai.

## Kontribusi

Kontribusi sangat terbuka. Jika Anda ingin meningkatkan aplikasi, beberapa ide yang bisa dikembangkan:

- menambahkan lebih banyak library latihan
- memperbaiki analitik workout
- menyempurnakan pelacakan nutrisi
- memperluas logika custom routine
- meningkatkan onboarding dan aksesibilitas
