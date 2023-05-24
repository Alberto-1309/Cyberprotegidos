package es.uca.cyberprotegidos.ui.salas

import android.content.Intent
import android.media.MediaScannerConnection
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ScrollView
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.layout.Document
import com.itextpdf.layout.element.Paragraph
import es.uca.cyberprotegidos.R
import es.uca.cyberprotegidos.databinding.FragmentSalasBinding
import java.io.File
import java.io.FileOutputStream

class SalasFragment : Fragment() {

    private var _binding: FragmentSalasBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val galleryViewModel =
            ViewModelProvider(this).get(SalasViewModel::class.java)

        _binding = FragmentSalasBinding.inflate(inflater, container, false)
        val root: View = binding.root


        // Envuelve el contenido en un ScrollView
        val scrollView = ScrollView(requireContext())
        scrollView.addView(root)

        return scrollView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val botonPDF: Button = view.findViewById(R.id.botonPDF)

        botonPDF.setOnClickListener {

            generarYDescargarPdf()
        }
    }





    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun generarYDescargarPdf() {
        val path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).path
        val archivoPdf = File(path)

        if (!archivoPdf.exists()) archivoPdf.mkdirs()
        val archivo = File(archivoPdf, "salas.pdf")
        val fileOutputStream = FileOutputStream(archivo)

        val writer = PdfWriter(fileOutputStream)
        val pdf = PdfDocument(writer)
        val document = Document(pdf)

        try {
            document.add(Paragraph("                        SALAS DISPONIBLES:"))
            document.add(Paragraph("TUTORIA:"))
            document.add(Paragraph("La capacidad de nuestras tutorías están limitadas a grupos de 6 personas como máximo."))
            document.add(Paragraph("Pretendemos ayudar a las personas respondiendo sus dudas tanto individuales como colectivas. Se impartirán desde una plataforma online la cual detallaremos cuando aceptemos las reservas."))
            document.add(Paragraph("- Número de personas: 6"))
            document.add(Paragraph("- Conexión: Sí"))
            document.add(Paragraph("TALLER:"))
            document.add(Paragraph("La capacidad de los talleres están limitadas según el aforo máximo del aula donde impartamos el taller."))
            document.add(Paragraph("Pretendemos dar un taller educativo para un numero elevado de personas con el fin de enseñar a como evitar diferentes ciberataques y como se pueden solucionar. Se impartirán en un aula con una capacidad de unas 20–30 personas."))
            document.add(Paragraph("- Número de personas: 30"))
            document.add(Paragraph("- Conexión: Sí"))
            document.add(Paragraph("CONGRESO:"))
            document.add(Paragraph("La capacidad de las personas que puedan asistir a los congresos dependerá del sitio donde se celebre."))
            document.add(Paragraph("Pretendemos que en dicho congreso podamos exponer ciberataques que se hayan producido a nivel global e incluso investigaciones propias. Se impartirán en auditorios, centros que tengan como mínimo una capacidad de 200 personas."))
            document.add(Paragraph("- Número de personas: 200"))
            document.add(Paragraph("- Conexión: Sí"))

            document.close()

            MediaScannerConnection.scanFile(context, arrayOf(archivoPdf.path), null, null)

            Toast.makeText(context, "PDF descargado correctamente", Toast.LENGTH_SHORT).show()
            Log.d("PDF", "Ruta del archivo PDF: ${archivo.absolutePath}")

            // Descargar el archivo PDF
            val intent = Intent(Intent.ACTION_VIEW)
            val uri = FileProvider.getUriForFile(
                requireContext(),
                requireContext().packageName + ".provider",
                archivo
            )
            intent.setDataAndType(uri, "application/pdf")
            intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
            startActivity(Intent.createChooser(intent, "Abrir con"))

        } catch (e: Exception) {
        }
    }

}
