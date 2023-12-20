import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.example.loketkereta.BookingDetailActivity
import com.example.loketkereta.R
import com.example.loketkereta.databinding.ListJadwalKeretaBinding
import com.example.loketkereta.kereta.dataKereta

class KeretaAdapter(private val context: Context, private val arrayList: ArrayList<dataKereta>) : ArrayAdapter<dataKereta>(context, R.layout.list_jadwal_kereta, arrayList) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val binding = ListJadwalKeretaBinding.inflate(LayoutInflater.from(context), parent, false)

        val kereta = arrayList[position]
        binding.hargaTiket.text = kereta.harga
        binding.stasiunKeberangkatan.text = kereta.stasiunKeberangkatan
        binding.jamBerangkat.text = kereta.jamBerangkat
        binding.sisaKursi.text = kereta.sisaTiket
        binding.namaKereta.text = kereta.namaKereta
        binding.stasiunTujuan.text = kereta.stasiunTujuan
        binding.jamTiba.text = kereta.jamTiba
        binding.kelasKereta.text = kereta.kelasKereta
        binding.waktuPerjalanan.text = kereta.durasiPerjalanan

        binding.root.setOnClickListener {
            val intent = Intent(context, BookingDetailActivity::class.java)
            intent.putExtra("kereta", kereta)
            context.startActivity(intent)
        }

        return binding.root
    }
}