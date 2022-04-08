package code.utils

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Typeface
import android.media.ExifInterface
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.os.Handler
import android.provider.MediaStore.Files.FileColumns
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.util.Base64
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import code.view.BaseActivity
import com.amtz.v2home.R
import com.squareup.picasso.Picasso
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern

//Common public methods
object AppUtils {
    var mToast: Toast? = null
    private val SECOND_MILLIS = 1000
    private val MINUTE_MILLIS = 60 * SECOND_MILLIS
    private val HOUR_MILLIS = 60 * MINUTE_MILLIS
    private val DAY_MILLIS = 24 * HOUR_MILLIS
    var progressDialog: ProgressDialog? = null
    fun print(mString: String): String {
        return mString
    }


    fun returnDouble(s: String): Double {
        return if (s.isEmpty()) 0.0 else {
            var amount = 0.0
            try {
                amount = s.toDouble()
            } catch (e: java.lang.NumberFormatException) {
                e.printStackTrace()
            }
            amount
        }
    }


    fun hideSoftKeyboard(activity: Activity?) {
        if (activity != null) {
            try {
                @SuppressLint("WrongConstant") val inputMethodManager =
                    activity.getSystemService("input_method") as InputMethodManager
                val view = activity.currentFocus
                if (view != null) {
                    val binder = view.windowToken
                    if (binder != null) {
                        inputMethodManager.hideSoftInputFromWindow(binder, 0)
                    }
                }
            } catch (e: Exception) {
                Log.v("gdfdsa",e.message.toString())
                e.printStackTrace()
            }
        }
        activity!!.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
    }

    fun getDisplayMetrics(context: Context): DisplayMetrics {
        return context.resources.displayMetrics
    }

    fun convertDpToPixel(dp: Float, context: Context): Float {
        return getDisplayMetrics(context).densityDpi.toFloat() / 160.0f * dp
    }

    fun convertDpToPixelSize(dp: Float, context: Context): Int {
        val pixels = convertDpToPixel(dp, context)
        val res = (0.5f + pixels).toInt()
        if (res != 0) {
            return res
        }
        if (pixels == 0.0f) {
            return 0
        }
        return if (pixels > 0.0f) {
            1
        } else -1
    }

    fun setCustomFont(mActivity: Activity, mTextView: TextView, asset: String?) {
        mTextView.setTypeface(Typeface.createFromAsset(mActivity.assets, asset))
    }

    fun showRequestDialog(activity: Activity) {
        try {
            if (!activity.isFinishing) {
                if (progressDialog != null && progressDialog!!.isShowing) {
                    progressDialog!!.dismiss()
                    progressDialog = null
                }
                progressDialog = ProgressDialog(activity)
                progressDialog!!.setCancelable(false)
                progressDialog!!.setMessage(activity.getString(R.string.pleaseWait))
                progressDialog!!.setProgressStyle(android.R.style.Widget_ProgressBar_Small)
                progressDialog!!.show()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun hideDialog() {
        try {
            if (progressDialog != null && progressDialog!!.isShowing) {
                progressDialog!!.dismiss()
                progressDialog = null
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun toCamelCaseWord(word: String?): String {
        if (word == null) {
            return ""
        }
        when (word.length) {
            0 -> return ""
            1 -> return word.toUpperCase(Locale.getDefault()) + " "
            else -> return Character.toUpperCase(word[0]).toString() + word.substring(1)
                .toLowerCase(
                    Locale.getDefault()
                ) + " "
        }
    }

    fun isEmailValid(email: String): Boolean {
        var isValid = false
        val expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$"
        val inputStr: CharSequence = email
        val pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE)
        val matcher = pattern.matcher(inputStr)
        if (matcher.matches()) {
            isValid = true
        }
        return isValid
    }

    fun getMMMddFromDate(prev_date: String?): String? {
        val originalFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
        val targetFormat: DateFormat = SimpleDateFormat("MMM dd")
        var date: Date? = null
        try {
            date = originalFormat.parse(prev_date)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        var formattedDate: String? = ""
        if (date != null) formattedDate = targetFormat.format(date) else formattedDate = prev_date
        return formattedDate
    }

    fun getddMMMFromDate(prev_date: String?): String? {
        val originalFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
        val targetFormat: DateFormat = SimpleDateFormat("dd MMM")
        var date: Date? = null
        try {
            date = originalFormat.parse(prev_date)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        var formattedDate: String? = ""
        if (date != null) formattedDate = targetFormat.format(date) else formattedDate = prev_date
        return formattedDate
    }

    fun md5(str: String): String {
        val MD5 = "MD5"
        try {
            // Create MD5 Hash
            val digest = MessageDigest.getInstance(MD5)
            digest.update(str.toByteArray())
            val messageDigest = digest.digest()

            // Create Hex String
            val hexString = StringBuilder()
            for (aMessageDigest: Byte in messageDigest) {
                var h = Integer.toHexString(0xFF and aMessageDigest.toInt())
                while (h.length < 2) h = "0$h"
                hexString.append(h)
            }

            //Log.v("md5",hexString.toString());
            return hexString.toString()
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        }
        return ""
    }

    /**
     * This method can be check internet connection is available or not.
     *
     * @param mActivity reference of activity.
     * @return
     */
    fun isNetworkAvailable(mActivity: Context): Boolean {
        var available = false

        /** Getting the system's connectivity service  */
        val cm = mActivity.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        /** Getting active network interface to get the network's staffMobile  */
        val activeNetwork = cm.activeNetworkInfo
        if (activeNetwork != null) { // connected to the internet
            if (activeNetwork.type == ConnectivityManager.TYPE_WIFI) {
                // connected to wifi
                available = true
                print("====activeNetwork" + activeNetwork.typeName)
            } else if (activeNetwork.type == ConnectivityManager.TYPE_MOBILE) {
                // connected to the mobile provider's data plan
                available = true
                print("====activeNetwork" + activeNetwork.typeName)
            }
        } else {
            // not connected to the internet
            available = false
            print("====not connected to the internet")
        }
        /** Returning the staffMobile of the network  */
        return available
    }

    fun getDateTimeFromStamp(timestamp: String?): String {
        val c = Calendar.getInstance()
        c.timeInMillis = java.lang.Long.valueOf(timestamp) * 1000
        val d = c.time
        val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
        return sdf.format(d)
    }

    fun getMonthFromDate(prev_date: String?): String {
        val originalFormat: DateFormat =
            SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH)
        val targetFormat: DateFormat = SimpleDateFormat("MM")
        var date: Date? = null
        try {
            date = originalFormat.parse(prev_date)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return targetFormat.format(date)
    }

    fun getImageOrientation(imagePath: String?): Int {
        var rotate = 0
        try {
            val imageFile = File(imagePath)
            val exif = ExifInterface(
                imageFile.absolutePath
            )
            val orientation = exif.getAttributeInt(
                ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_NORMAL
            )
            when (orientation) {
                ExifInterface.ORIENTATION_ROTATE_270 -> rotate = 270
                ExifInterface.ORIENTATION_ROTATE_180 -> rotate = 180
                ExifInterface.ORIENTATION_ROTATE_90 -> rotate = 90
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return rotate
    }

    fun getFileType(path: String): String {
        var fileType: String? = null
        fileType = path.substring(path.indexOf('.', path.lastIndexOf('/')) + 1).toLowerCase()
        return fileType
    }

    fun getEncoded64ImageStringFromBitmap(bitmap: Bitmap): String {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream)
        val byteFormat = stream.toByteArray()
        // get the base 64 string
        return Base64.encodeToString(byteFormat, Base64.NO_WRAP)
    }

    fun getOutputMediaFile(type: Int): File? {
        // External sdcard location
        val mediaStorageDir = File(
            Environment
                .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
            "AIPHC"
        )

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(
                    "AIPHC", "Oops! Failed create "
                            + "AIPHC" + " directory"
                )
                return null
            }
        }

        // Create a media file name
        val timeStamp = SimpleDateFormat(
            "yyyyMMdd_HHmmss",
            Locale.getDefault()
        ).format(Date())
        val mediaFile: File
        if (type == FileColumns.MEDIA_TYPE_IMAGE) {
            mediaFile = File(
                mediaStorageDir.path + File.separator
                        + "IMG_" + timeStamp + ".jpg"
            )
        } else if (type == FileColumns.MEDIA_TYPE_VIDEO) {
            mediaFile = File(
                (mediaStorageDir.path + File.separator
                        + "VID_" + timeStamp + ".mp4")
            )
        } else {
            return null
        }
        return mediaFile
    }

    /* Creating file uri to store image/video*/
    fun getOutputMediaFileUri(type: Int, mActivity: Activity): Uri {
        return FileProvider.getUriForFile(
            mActivity, mActivity.packageName + ".provider",
            (getOutputMediaFile(type))!!
        )
    }

    fun checkAndRequestPermissions(mActivity: Activity?): Boolean {
        val writeStorage = ContextCompat.checkSelfPermission(
            (mActivity)!!,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        val openCamera = ContextCompat.checkSelfPermission((mActivity), Manifest.permission.CAMERA)
        val listPermissionsNeeded: MutableList<String> = ArrayList()
        if (openCamera != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA)
        }
        if (writeStorage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(
                (mActivity)!!, listPermissionsNeeded.toTypedArray(),
                1
            )
            return false
        }
        return true
    }

    fun getDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val theta = lon1 - lon2
        var dist = (Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + (Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta))))
        dist = Math.acos(dist)
        dist = rad2deg(dist)
        dist = dist * 60 * 1.1515
        return (dist)
    }

    fun deg2rad(deg: Double): Double {
        return (deg * Math.PI / 180.0)
    }

    fun rad2deg(rad: Double): Double {
        return (rad * 180.0 / Math.PI)
    }

    fun showDateDialog(editText: EditText, mActivity: Activity?) {
        val mYear: Int
        val mMonth: Int
        val mDay: Int

        // Get Current Date
        val c = Calendar.getInstance()
        mYear = c[Calendar.YEAR]
        mMonth = c[Calendar.MONTH]
        mDay = c[Calendar.DAY_OF_MONTH]
        val datePickerDialog = DatePickerDialog(
            (mActivity)!!,
            { view, year, monthOfYear, dayOfMonth ->
                var month = ""
                month = (monthOfYear + 1).toString()
                var day = dayOfMonth.toString()
                if (monthOfYear + 1 < 10) month = "0$month"
                if (dayOfMonth < 10) day = "0$day"
                editText.setText("$day-$month-$year")
            },
            mYear, mMonth, mDay
        )
        datePickerDialog.datePicker.maxDate = c.timeInMillis
        datePickerDialog.show()
    }

    fun disableEnableView(view: View) {
        view.isEnabled = false
        Handler().postDelayed({ view.isEnabled = true }, 500)
    }

    fun loadPicassoProfile(url: String, imageView: ImageView?) {
        if (!url.isEmpty()) Picasso.get().load(url).placeholder(R.drawable.ic_person)
            .into(imageView)
    }

    fun loadPicassoImage(url: String, imageView: ImageView?) {
        if (!url.isEmpty()) Picasso.get().load(url).into(imageView)
    }

    fun checkPermissions(mActivity: Activity): Boolean {
        if (ActivityCompat.checkSelfPermission(
                mActivity,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) !=
            PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                mActivity,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) !=
            PackageManager.PERMISSION_GRANTED
        ) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                mActivity.requestPermissions(
                    arrayOf(
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ), 1
                )
            }
            return false
        }
        return true
    }

    fun makeCall(mActivity: Activity, number: String) {
        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$number"))
        mActivity.startActivity(intent)
    }

    val uniqueId: String
        get() {
            val uuid = UUID.randomUUID()
            return uuid.toString()
        }

    fun ifEmptyReturn0(s: String): String {
        return if (s.isEmpty()) "0" else s
    }

    fun ifMoreThan1ReturnWithS(no: String, s: String): String {
        return if ((no == "0") || (no == "1") || no.isEmpty()) s else s + "s"
    }

    fun ifEmptyReturnNa(s: String): String {
        return if (s.isEmpty()) "N/A" else s
    }


    fun if0ReturnNa(s: String): String {
        return if ((s == "0")) "N/A" else s
    }

    fun returnStringToInt(s: String): Int {
        var quantity = 0
        return if (s.isEmpty()) quantity else {
            try {
                quantity = s.toInt()
                quantity
            } catch (e: NumberFormatException) {
                e.printStackTrace()
                0
            }
        }
    }


    fun isPackageInstalled(packageName: String?, mActivity: Activity): Boolean {
        try {
            val pm = mActivity.packageManager
            pm.getPackageInfo((packageName)!!, 0)
            return true
        } catch (e: PackageManager.NameNotFoundException) {
            return false
        }
    }

    fun setSpannable(str1: String, str2: String, mActivity: BaseActivity): SpannableStringBuilder {
        val text = "$str1 $str2"
        val ssBuilder = SpannableStringBuilder(text)
        Log.d("asdf-1", text + " " + text.length)
        Log.d("asdf-2", str1 + " " + str1.length)
        Log.d("asdf-3", str2 + " " + str2.length)
        ssBuilder.setSpan(
            ForegroundColorSpan(mActivity.resources.getColor(R.color.textLight)),
            text.indexOf(str1),
            text.indexOf(str1) + str1.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        ssBuilder.setSpan(
            ForegroundColorSpan(mActivity.resources.getColor(R.color.colorPrimary)),
            str1.length,
            str1.length + 1 + str2.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        return ssBuilder
    }

    fun getDiffBetweenTime(stDnT: String?, endSnT: String?): Long {
        Log.v("lqjkshjklq", (stDnT)!!)
        //endSnT = endSnT + ", 00:00:00 AM";
        val simpleDateFormat = SimpleDateFormat("yyyy-mm-dd, HH:mm:ss")
        var startDate: Date? = null
        try {
            startDate = simpleDateFormat.parse(stDnT)
            Log.i("startDate", startDate.toString())
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        var endDate: Date? = null
        try {
            endDate = simpleDateFormat.parse(endSnT)
            Log.i("endDate", endDate.toString())
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        val difference = endDate!!.time - startDate!!.time
        Log.i("log_tag", "difference: $difference")
        return difference
    }

    val currentDate: String
        get() {
            val c = Calendar.getInstance().time
            println("Current time => $c")
            val df = SimpleDateFormat("yyyy-MM-dd")
            return df.format(c)
        }
    val currentDateTimeSecondss: String
        get() {
            val c = Calendar.getInstance().time
            println("Current time => $c")
            val df = SimpleDateFormat("yymmddHHmmss")
            return df.format(c)
        }

    fun isEmpty(editText: EditText): Boolean {
        return editText.text.toString().trim { it <= ' ' }.isEmpty()
    }


    val alphanumericvalue: String
        get() {
            val DATA = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ"
            val RANDOM = Random()
            val sb = StringBuilder(5)
            for (i in 0..7) {
                sb.append(DATA[RANDOM.nextInt(DATA.length)])
            }
            return sb.toString()
        }

}