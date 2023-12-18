import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.example.loketkereta.R
import com.example.loketkereta.databinding.ListJadwalKeretaBinding
import com.example.loketkereta.kereta.dataKereta

class KeretaAdapter(private val context: Activity, private val arrayList: ArrayList<dataKereta>) : ArrayAdapter<dataKereta>(context, R.layout.list_jadwal_kereta, arrayList) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val binding = ListJadwalKeretaBinding.inflate(LayoutInflater.from(context), parent, false)

        binding.hargaTiket.text = arrayList[position].harga
        binding.stasiunKeberangkatan.text = arrayList[position].keberangkatan
        binding.jamBerangkat.text = arrayList[position].jamBerangkat
        binding.sisaKursi.text = arrayList[position].sisa
        binding.namaKereta.text = arrayList[position].namaKereta
        binding.stasiunTujuan.text = arrayList[position].tujuan
        binding.jamTiba.text = arrayList[position].jamTiba
        binding.kelasKereta.text = arrayList[position].kelasKereta
        binding.waktuPerjalanan.text = arrayList[position].durasiPerjalanan

        return binding.root
    }
}