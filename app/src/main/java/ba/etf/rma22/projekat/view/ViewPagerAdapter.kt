package ba.etf.rma22.projekat.view

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import ba.etf.rma22.projekat.MainActivity

class ViewPagerAdapter(val items : ArrayList<Fragment>, activity: AppCompatActivity) : FragmentStateAdapter(activity){
    override fun getItemCount(): Int {
        return items.size
    }

    override fun createFragment(position: Int): Fragment {
       return items[position]
    }
    fun refreshFragment(index: Int, fragment: Fragment) {
        items[index] = fragment
        notifyItemChanged(index)
    }


}