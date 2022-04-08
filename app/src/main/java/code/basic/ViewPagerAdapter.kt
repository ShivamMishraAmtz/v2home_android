package code.basic

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.viewpager.widget.PagerAdapter
import code.common.BannerModal
import code.utils.AppUrls
import com.amtz.v2home.R
import com.squareup.picasso.Picasso
import java.util.*


class ViewPagerAdapter(var context: Context, var images: ArrayList<BannerModal>) :

    PagerAdapter() {
    var layoutInflater: LayoutInflater
    override fun getCount(): Int {
        return images.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object` as CardView
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val itemView: View = layoutInflater.inflate(R.layout.item, container, false)
        val imageView = itemView.findViewById<View>(R.id.imageViewMain) as ImageView
        Log.v("hnghbfgds",AppUrls.baseUrlForImage+images[position].images);
        Picasso.get().load(AppUrls.baseUrlForImage+images[position].images).into(imageView)
        container.addView(itemView)

        return itemView
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as CardView)
    }

    init {
        layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    }
}