package com.openclassrooms.realestatemanager.controller

import android.app.Activity
import android.content.Intent
import android.opengl.Visibility
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.openclassrooms.realestatemanager.Injections.Injection
import com.openclassrooms.realestatemanager.adapter.ItemHomeAdapter

import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.Utils.QueryBuilder
import com.openclassrooms.realestatemanager.Utils.RecyclerClickListener
import com.openclassrooms.realestatemanager.Utils.Utils
import com.openclassrooms.realestatemanager.adapter.ItemCategoryAdapter
import com.openclassrooms.realestatemanager.model.EstateCategory
import com.openclassrooms.realestatemanager.model.entity.Estate
import com.openclassrooms.realestatemanager.viewModel.EstateViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_main.*
import java.util.*

class HomeFragment : Fragment(), RecyclerClickListener.OnEstateClick, RecyclerClickListener.OnItemClick {

    companion object{
        private const val RQ_FILTER_ACTIVITY = 5
        private const val LIST_FRAGMENT_TAG = "list_fragment_tag"
        private const val DETAIL_FRAGMENT_TAG = "detail_fragment_tag"
    }

    private lateinit var rootView: View
    private lateinit var itemRecycler: RecyclerView
    private lateinit var gridLayoutManager: GridLayoutManager
    private lateinit var estateAdapter: ItemHomeAdapter
    private lateinit var categoryRecycler: RecyclerView
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var categoryAdapter: ItemCategoryAdapter
    private lateinit var viewModel: EstateViewModel
    private lateinit var listFragment: ListFragment
    private lateinit var detailFragment: DetailFragment

    //private var viewModel = estateViewModel
    private var estates = ArrayList<Estate>()
    private var categories = ArrayList<EstateCategory>()
    private var isDollar = true

    // Callback
    private val callback: RecyclerClickListener.OnEstateClick  = this
    private val categoryCallback: RecyclerClickListener.OnItemClick = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        this.rootView = inflater.inflate(R.layout.fragment_main, container, false)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        this.initViewModel()
        this.recyclerViewItemConfig()
        this.addEstate()

        this.recyclerViewCategoryConfig()
        this.addCategory()

        this.onClickSearchView()
        this.onClickFloatingAddButton()
    }

    // Get the View Model
    private fun initViewModel(){
        val activity = activity as MainActivity
        if (activity.getViewModel() != null) {
            this.viewModel = activity.getViewModel()!!
        }
        else {
            val viewModelFactory = Injection.provideViewModelFactory(context!!)
            this.viewModel = ViewModelProviders.of(this, viewModelFactory).get(EstateViewModel::class.java)
        }
    }

    // Configure RecyclerView of estates items
    private fun recyclerViewItemConfig(){
        this.itemRecycler = fragment_main_recyclerview_sold
        this.estateAdapter = ItemHomeAdapter(estates,callback)
        this.itemRecycler.layoutManager = this.getGridLayoutManager()
        this.itemRecycler.adapter = this.estateAdapter
    }

    private fun getGridLayoutManager(): GridLayoutManager{
       if (resources.getBoolean(R.bool.isTabletMode)){
           this.gridLayoutManager = GridLayoutManager(context, 1)
        }
        else{
           this.gridLayoutManager = GridLayoutManager(context, 2)
       }
        return this.gridLayoutManager
    }

    // Configure RecyclerView of categories
    private fun recyclerViewCategoryConfig(){
        this.categoryRecycler = fragment_main_recyclerview_categories
        this.linearLayoutManager = LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)
        this.categoryAdapter = ItemCategoryAdapter(categories, categoryCallback)
        this.categoryRecycler.layoutManager = this.linearLayoutManager
        this.categoryRecycler.adapter = this.categoryAdapter
    }

    private fun showDetailFragment(estate: Estate){
        if (resources.getBoolean(R.bool.isTabletMode) || resources.getBoolean(R.bool.isTabletLandMode)){
            val bundle = Bundle()
            bundle.putLong("estate_id", estate.estateUid)

            val fragment = activity?.supportFragmentManager?.findFragmentByTag(DETAIL_FRAGMENT_TAG)

            if (fragment != null) this.detailFragment = fragment as DetailFragment
            else this.detailFragment = DetailFragment()

            this.detailFragment.arguments = bundle
            activity?.supportFragmentManager?.beginTransaction()?.replace(R.id.activity_main_framelayout_list_2, this.detailFragment)?.commit()

            activity_main_framelayout_list_2_no_selection.visibility = View.GONE // Hide no selection message
        }
        else {
            startActivity(Intent(context, DetailActivity::class.java).putExtra("ESTATE_ID",estate.estateUid))
        }
    }

    // Start FilterActivity when search bar clicked
    private fun onClickSearchView(){
        fragment_main_search_view.setOnClickListener {
            val intent = Intent(context,FilterActivity::class.java)
            startActivityForResult(intent,RQ_FILTER_ACTIVITY)
        }
    }

    // Start NewEstateActivity when floating btn clicked
    private fun onClickFloatingAddButton(){
        activity_main_floating_btn.setOnClickListener {
            startActivity(Intent(context, NewEstateActivity::class.java))
        }
    }

    override fun onEstateItemClick(estate: Estate) {
        this.showDetailFragment(estate)
        for (i in 0 until estate.images.size) { Log.d("URI ", estate.images[i].path) }
    }

    override fun onItemClick(position: Int) {
        val estateCategory = this.categoryAdapter.getItem(position)
        val queryString = QueryBuilder.getCategoryQuery(estateCategory.name)
        this.displayListFragment(queryString)
    }

    private fun addEstate(){
        this.getAllEstate()
    }

    private fun getAllEstate(){
        this.viewModel.getLatestEstate()?.observe(this, Observer { list->
            estates.clear()
            estates.addAll(list)
            estateAdapter.notifyDataSetChanged()
        })
    }

    private fun addCategory(){
        this.categories.clear()
        val cat = resources.getStringArray(R.array.categoryArray)
        var i = 0
        cat.forEach { str ->
            categories.add(EstateCategory(i.toLong(),str))
            i++
        }
    }

    private fun displayListFragment(query: String){
        Log.d(this.javaClass.simpleName, "Query = $query")

        if (activity?.supportFragmentManager?.findFragmentByTag(LIST_FRAGMENT_TAG) != null)
           this.listFragment = activity?.supportFragmentManager?.findFragmentByTag(LIST_FRAGMENT_TAG) as ListFragment
        else
            this.listFragment = ListFragment()

        val arg = Bundle()
        arg.putString("query", query)
        this.listFragment.arguments = arg

        activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.activity_main_framelayout_list, this.listFragment, LIST_FRAGMENT_TAG)
                ?.addToBackStack(null)
                ?.commit()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            RQ_FILTER_ACTIVITY ->
                when (resultCode) {
                    Activity.RESULT_OK -> {
                        Log.d(this.javaClass.simpleName, "onActivityResult -> RESULT OK")
                        if (data != null) displayListFragment(QueryBuilder().getQuery(data))
                    }

                    Activity.RESULT_CANCELED -> {
                        Log.d(this.javaClass.simpleName, "onActivityResult -> RESULT CANCELED")
                    }
                }
        }
    }

    //***************************** MENU + ACTIONS ****************************

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.conversion_toolbar_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.conversion_dollars_euros -> this.convertIntoEuros()
            R.id.conversion_euros_dollars -> this.convertIntoDollars()
        }
        return false
    }

    private fun convertIntoDollars(){
        if (!isDollar) {
            this.isDollar = true
            this.estates.forEach {estate ->
                estate.price = Utils.convertEuroToDollar((estate.price.toInt())).toLong()
                this.estateAdapter.setDollarCurrency()
                this.estateAdapter.notifyDataSetChanged()
            }
        }
    }

    private fun convertIntoEuros(){
       if (isDollar) {
           this.isDollar = false
           this.estates.forEach {estate ->
               estate.price = Utils.convertDollarToEuro((estate.price.toInt())).toLong()
               this.estateAdapter.setEuroCurrency()
               this.estateAdapter.notifyDataSetChanged()
           }
       }
    }
}
