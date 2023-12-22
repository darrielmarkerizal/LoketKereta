package com.example.loketkereta

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.loketkereta.databinding.ActivityBookingSuccessBinding
import com.example.loketkereta.kereta.dataKereta
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class BookingSuccessActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBookingSuccessBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBookingSuccessBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val kereta = intent.getSerializableExtra("kereta") as dataKereta

        binding.jamKeberangkatan.text = kereta.jamBerangkat
        binding.jamTiba.text = kereta.jamTiba
        binding.kotaKeberangkatan.text = kereta.stasiunKeberangkatan
        binding.kotaTiba.text = kereta.stasiunTujuan

        val originalFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        val targetFormat = SimpleDateFormat("EEEE, dd MMM yy", Locale("in"))
        val date = originalFormat.parse(kereta.tanggalBerangkat)
        val formattedDate = targetFormat.format(date)
        binding.tanggalKeberangkatan.text = formattedDate
        binding.tanggalKeberangkatan.text = kereta.tanggalBerangkat
        binding.waktuKeberangkatan.text = kereta.jamBerangkat

        val buttonSelesai = binding.btnSelesai
        buttonSelesai.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)

            // Create a notification channel
            val channelId = "booking_success_channel"
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val name = "Booking Success"
                val descriptionText = "Notification for successful booking"
                val importance = NotificationManager.IMPORTANCE_DEFAULT
                val channel = NotificationChannel(channelId, name, importance).apply {
                    description = descriptionText
                }
                val notificationManager: NotificationManager =
                    getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.createNotificationChannel(channel)
            }

            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.US)
            val departureDate = sdf.parse(kereta.tanggalBerangkat)
            val currentDate = Date()
            val diff = departureDate.time - currentDate.time
            val diffDays = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS)

            // Create a notification
            val builder = NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.notification_icon)
                .setContentTitle("Booking Success")
                .setContentText("You have successfully booked ${kereta.namaKereta} for route ${kereta.stasiunKeberangkatan} - ${kereta.stasiunTujuan}. Your journey will start in $diffDays days.")
                .setPriority(NotificationCompat.PRIORITY_HIGH)

            showNotification(builder)
        }
    }

    @SuppressLint("MissingPermission")
    fun showNotification(builder: NotificationCompat.Builder) {
        with(NotificationManagerCompat.from(this@BookingSuccessActivity)) {
            notify(0, builder.build())
        }
    }
}