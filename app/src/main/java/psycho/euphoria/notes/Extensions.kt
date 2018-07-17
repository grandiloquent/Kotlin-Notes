package psycho.euphoria.notes

import android.content.Context
import android.content.Intent
import android.graphics.*
import android.net.Uri
import android.support.annotation.DrawableRes
import android.support.annotation.StringRes
import android.support.graphics.drawable.VectorDrawableCompat
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import android.widget.ImageView
import android.support.v4.graphics.TypefaceCompatUtil.closeQuietly
import android.util.Base64
import android.util.Base64OutputStream
import java.io.*


fun ImageView.setVectorCompat(@DrawableRes drawable: Int, tint: Int? = null) {
    val vector = VectorDrawableCompat.create(resources, drawable, context.theme)
    if (tint != null) {
        vector?.mutate()
        vector?.setTint(tint)
    }
}


fun Context.getResourceColor(@StringRes resource: Int): Int {
    val typedArray = obtainStyledAttributes(intArrayOf(resource))
    val attrValue = typedArray.getColor(0, 0)
    typedArray.recycle()
    return attrValue
}

fun Context.drawText(str: String,
                     width: Int,
                     fileName: File,
                     titleSize: Float = 30.0f,
                     textSize: Float = 22.0f,
                     margin: Int = 30) {
    if (str.isNullOrBlank()) return

    val lines = str.trim().split(Regex("\n"), 2)
    val title = lines[0]
    val content = if (lines.size > 1) lines[1] else lines[0]
    val textPaint = TextPaint().apply {
        isAntiAlias = true
    }

    textPaint.textSize = titleSize
    val titleLayout = StaticLayout(title, textPaint, width, Layout.Alignment.ALIGN_CENTER, 1.25f, 0.0f, false);
    textPaint.textSize = textSize
    val contentLayout = StaticLayout(content, textPaint, width, Layout.Alignment.ALIGN_NORMAL, 1f, 0.0f, false)


    val height = titleLayout.height + (margin shl 1) + contentLayout.height


    val bitmap = Bitmap.createBitmap(width + (margin shl 1), height + margin + margin * 3, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)
    canvas.drawColor(Color.WHITE)
    textPaint.color = 0XFF666666.toInt()

    canvas.translate(margin.toFloat(), margin.toFloat())
    textPaint.textSize = titleSize
    titleLayout.draw(canvas)

    canvas.translate(0.0f, (titleLayout.height + margin).toFloat())

    val paint = Paint()
    paint.setColor(Color.BLACK)
    paint.setStrokeWidth(3.0f)
    paint.isAntiAlias = true
    paint.setPathEffect(DashPathEffect(floatArrayOf(5f, 10f), 0f))
    canvas.drawLine(0.0f, 0.0f, width.toFloat(), 0.0f, paint)

    canvas.translate(0.0f, (margin shr 1).toFloat())
    textPaint.textSize = textSize
    contentLayout.draw(canvas)
    canvas.drawLine(0.0f, (contentLayout.height + 15 + margin).toFloat(), width.toFloat(), (contentLayout.height + 15 + margin).toFloat(), paint)

    saveJpg(bitmap, this, fileName)

}

fun Context.triggerScanFile(file: File) {
    val mediaScanIntent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
    val uri = Uri.fromFile(file)
    mediaScanIntent.data = uri
    sendBroadcast(mediaScanIntent)

}


private fun saveJpg(bitmap: Bitmap, context: Context, file: File) {


    var os: FileOutputStream

    try {
        os = FileOutputStream(file)

        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, os)
        context.triggerScanFile(file)
        os.close()
        bitmap.recycle()
    } catch (e: Exception) {
        e.printStackTrace()
    }

}