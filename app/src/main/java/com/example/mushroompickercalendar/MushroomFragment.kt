package com.example.mushroompickercalendar

import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.view.*
import android.widget.*
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import java.io.File
import java.util.*

class MushroomFragment : Fragment() {

    private var mMushroom: Mushroom? = null
    private var mFromRecyclerView: Boolean = false
    private var mEdit: Boolean = false
    private lateinit var mMushroomName: EditText
    private lateinit var mMushroomImage: ImageView
    private lateinit var mMushroomButton: ImageButton
    private lateinit var mPhotoFile: File

    private lateinit var mJanuaryCheckbox: CheckBox
    private lateinit var mFebruaryCheckbox: CheckBox
    private lateinit var mMarchCheckbox: CheckBox
    private lateinit var mAprilCheckbox: CheckBox
    private lateinit var mMayCheckbox: CheckBox
    private lateinit var mJuneCheckbox: CheckBox
    private lateinit var mJulyCheckbox: CheckBox
    private lateinit var mAugustCheckbox: CheckBox
    private lateinit var mSeptemberCheckbox: CheckBox
    private lateinit var mOctoberCheckbox: CheckBox
    private lateinit var mNovemberCheckbox: CheckBox
    private lateinit var mDecemberCheckbox: CheckBox

    private lateinit var mMixedCheckbox: CheckBox
    private lateinit var mDeciduousCheckbox: CheckBox
    private lateinit var mConiferousCheckbox: CheckBox

    private lateinit var mFryCheckbox: CheckBox
    private lateinit var mDryCheckbox: CheckBox
    private lateinit var mCookCheckbox: CheckBox
    private lateinit var mPickleCheckbox: CheckBox

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val mushroomId = arguments?.getSerializable(ARG_MUSHROOM_ID) as UUID
        mMushroom = MushroomData.get(context!!).getMushroom(mushroomId)
        mPhotoFile = MushroomData.get(context!!).getPhotoFile(mMushroom!!)
        mFromRecyclerView = arguments?.getSerializable(ARG_RECYCLER_VIEW) as Boolean
        if (!mFromRecyclerView) {
            setHasOptionsMenu(true)
        }
    }

    override fun onPause() {
        super.onPause()
        MushroomData.get(context!!).updateMushroom(mMushroom!!)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.mushroom_fragment, container, false)

//Image
        mMushroomImage = view.findViewById(R.id.mushroom_image)
        updateMushroomImage()
        mMushroomImage.setOnClickListener {
            if (!mEdit) {
                val intentImage = Intent(context!!, MushroomImageActivity::class.java)
                intentImage.putExtra(MushroomImageActivity.EXTRA_MUSHROOM_ID, mMushroom?.mId)
                startActivity(intentImage)
            }
        }

        mMushroomButton = view.findViewById(R.id.mushroom_camera)
        val captureImage = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val canTakePhoto =
            mPhotoFile != null && captureImage.resolveActivity(activity?.packageManager!!) != null
        mMushroomButton.isEnabled = canTakePhoto
        mMushroomButton.setOnClickListener {
            val uri = FileProvider.getUriForFile(context!!, FILE_PROVIDER, mPhotoFile)
            captureImage.putExtra(MediaStore.EXTRA_OUTPUT, uri)
            val cameraActivities = activity?.packageManager?.queryIntentActivities(
                captureImage,
                PackageManager.MATCH_DEFAULT_ONLY
            )
            for (activity: ResolveInfo in cameraActivities!!) {
                getActivity()?.grantUriPermission(
                    activity.activityInfo.packageName,
                    uri,
                    Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                )
            }
            startActivityForResult(captureImage, REQUEST_PHOTO)
        }


//Name
        mMushroomName = view.findViewById(R.id.mushroom_name)
        mMushroomName.setText(mMushroom?.mName)
        mMushroomName.filters = arrayOf(
            InputFilter { source, start, end, dest, dstart, dend ->
                return@InputFilter source.replace(Regex("[^a-zA-Z ]*"), "")
            }
        )
        mMushroomName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence,
                start: Int,
                before: Int,
                count: Int
            ) {
                mMushroom?.mName = s.toString()
            }

            override fun afterTextChanged(s: Editable) {}
        })

//Months
        mJanuaryCheckbox = view.findViewById(R.id.january_checkbox)
        mFebruaryCheckbox = view.findViewById(R.id.february_checkbox)
        mMarchCheckbox = view.findViewById(R.id.march_checkbox)
        mAprilCheckbox = view.findViewById(R.id.april_checkbox)
        mMayCheckbox = view.findViewById(R.id.may_checkbox)
        mJuneCheckbox = view.findViewById(R.id.june_checkbox)
        mJulyCheckbox = view.findViewById(R.id.july_checkbox)
        mAugustCheckbox = view.findViewById(R.id.august_checkbox)
        mSeptemberCheckbox = view.findViewById(R.id.september_checkbox)
        mOctoberCheckbox = view.findViewById(R.id.october_checkbox)
        mNovemberCheckbox = view.findViewById(R.id.november_checkbox)
        mDecemberCheckbox = view.findViewById(R.id.december_checkbox)

        restoreMonthsCheckbox()

        var grid = view.findViewById<GridLayout>(R.id.mushroom_grid_layout)
        var childCount = grid.childCount
        for (i in 0 until childCount) {
            val container = grid.getChildAt(i) as CheckBox
            container.setOnClickListener {
                concatenateMonths()
            }
        }

//Forest type
        mMixedCheckbox = view.findViewById(R.id.mixed_checkbox)
        mDeciduousCheckbox = view.findViewById(R.id.deciduous_checkbox)
        mConiferousCheckbox = view.findViewById(R.id.coniferous_checkbox)

        restoreForestTypeCheckbox()

        grid = view.findViewById<GridLayout>(R.id.forest_type_grid_layout)
        childCount = grid.childCount
        for (i in 0 until childCount) {
            val container = grid.getChildAt(i) as CheckBox
            container.setOnClickListener {
                concatenateForestType()
            }
        }

//Cooking method
        mFryCheckbox = view.findViewById(R.id.fry_checkbox)
        mDryCheckbox = view.findViewById(R.id.dry_checkbox)
        mCookCheckbox = view.findViewById(R.id.cook_checkbox)
        mPickleCheckbox = view.findViewById(R.id.pickle_checkbox)

        restoreCookingMethod()

        grid = view.findViewById<GridLayout>(R.id.cooking_method_grid_layout)
        childCount = grid.childCount
        for (i in 0 until childCount) {
            val container = grid.getChildAt(i) as CheckBox
            container.setOnClickListener {
                concatenateCookingMethod()
            }
        }

        if (mFromRecyclerView || mEdit) {
            enableCheckbox(true)
        } else {
            enableCheckbox(false)
        }

        return view
    }

    private fun enableCheckbox(isEnabled: Boolean) {
        mMushroomName.isEnabled = isEnabled

        mJanuaryCheckbox.isEnabled = isEnabled
        mFebruaryCheckbox.isEnabled = isEnabled
        mMarchCheckbox.isEnabled = isEnabled
        mAprilCheckbox.isEnabled = isEnabled
        mMayCheckbox.isEnabled = isEnabled
        mJuneCheckbox.isEnabled = isEnabled
        mJulyCheckbox.isEnabled = isEnabled
        mAugustCheckbox.isEnabled = isEnabled
        mSeptemberCheckbox.isEnabled = isEnabled
        mOctoberCheckbox.isEnabled = isEnabled
        mNovemberCheckbox.isEnabled = isEnabled
        mDecemberCheckbox.isEnabled = isEnabled

        mMixedCheckbox.isEnabled = isEnabled
        mDeciduousCheckbox.isEnabled = isEnabled
        mConiferousCheckbox.isEnabled = isEnabled

        mFryCheckbox.isEnabled = isEnabled
        mDryCheckbox.isEnabled = isEnabled
        mCookCheckbox.isEnabled = isEnabled
        mPickleCheckbox.isEnabled = isEnabled
    }

    private fun restoreCookingMethod() {
        val cookingMethodList = mMushroom?.getCookingMethodList()
        if (cookingMethodList == null) return
        for (cookingMethod in cookingMethodList) {
            if (cookingMethod.equals(getString(R.string.dry))) mDryCheckbox.isChecked = true
            if (cookingMethod.equals(getString(R.string.fry))) mFryCheckbox.isChecked =
                true
            if (cookingMethod.equals(getString(R.string.cook))) mCookCheckbox.isChecked =
                true
            if (cookingMethod.equals(getString(R.string.pickle))) mPickleCheckbox.isChecked =
                true
        }
    }

    private fun restoreForestTypeCheckbox() {
        val forestTypeList = mMushroom?.getForestTypeList()
        if (forestTypeList == null) return
        for (forestType in forestTypeList) {
            if (forestType.equals(getString(R.string.mixed))) mMixedCheckbox.isChecked = true
            if (forestType.equals(getString(R.string.deciduous))) mDeciduousCheckbox.isChecked =
                true
            if (forestType.equals(getString(R.string.coniferous))) mConiferousCheckbox.isChecked =
                true
        }
    }

    private fun restoreMonthsCheckbox() {
        val monthsList = mMushroom?.getMonthsList()
        if (monthsList == null) return
        for (month in monthsList) {
            if (month.equals(getString(R.string.january))) mJanuaryCheckbox.isChecked = true
            if (month.equals(getString(R.string.february))) mFebruaryCheckbox.isChecked = true
            if (month.equals(getString(R.string.march))) mMarchCheckbox.isChecked = true
            if (month.equals(getString(R.string.april))) mAprilCheckbox.isChecked = true
            if (month.equals(getString(R.string.may))) mMayCheckbox.isChecked = true
            if (month.equals(getString(R.string.june))) mJuneCheckbox.isChecked = true
            if (month.equals(getString(R.string.july))) mJulyCheckbox.isChecked = true
            if (month.equals(getString(R.string.august))) mAugustCheckbox.isChecked = true
            if (month.equals(getString(R.string.september))) mSeptemberCheckbox.isChecked = true
            if (month.equals(getString(R.string.october))) mOctoberCheckbox.isChecked = true
            if (month.equals(getString(R.string.november))) mNovemberCheckbox.isChecked = true
            if (month.equals(getString(R.string.december))) mDecemberCheckbox.isChecked = true
        }
    }

    private fun concatenateCookingMethod() {
        var cookingMethod = ""

        if (mDryCheckbox.isChecked) cookingMethod =
            addString(cookingMethod, mDryCheckbox.text.toString())
        if (mFryCheckbox.isChecked) cookingMethod =
            addString(cookingMethod, mFryCheckbox.text.toString())
        if (mCookCheckbox.isChecked) cookingMethod =
            addString(cookingMethod, mCookCheckbox.text.toString())
        if (mPickleCheckbox.isChecked) cookingMethod =
            addString(cookingMethod, mPickleCheckbox.text.toString())

        mMushroom?.mCookingMethod = cookingMethod
    }

    private fun concatenateForestType() {
        var forestType = ""

        if (mMixedCheckbox.isChecked) forestType =
            addString(forestType, mMixedCheckbox.text.toString())
        if (mDeciduousCheckbox.isChecked) forestType =
            addString(forestType, mDeciduousCheckbox.text.toString())
        if (mConiferousCheckbox.isChecked) forestType =
            addString(forestType, mConiferousCheckbox.text.toString())

        mMushroom?.mForestType = forestType
    }

    private fun concatenateMonths() {
        var months = ""

        if (mJanuaryCheckbox.isChecked) months = addString(months, mJanuaryCheckbox.text.toString())
        if (mFebruaryCheckbox.isChecked) months =
            addString(months, mFebruaryCheckbox.text.toString())
        if (mMarchCheckbox.isChecked) months = addString(months, mMarchCheckbox.text.toString())
        if (mAprilCheckbox.isChecked) months = addString(months, mAprilCheckbox.text.toString())
        if (mMayCheckbox.isChecked) months = addString(months, mMayCheckbox.text.toString())
        if (mJuneCheckbox.isChecked) months = addString(months, mJuneCheckbox.text.toString())
        if (mJulyCheckbox.isChecked) months = addString(months, mJulyCheckbox.text.toString())
        if (mAugustCheckbox.isChecked) months = addString(months, mAugustCheckbox.text.toString())
        if (mSeptemberCheckbox.isChecked) months =
            addString(months, mSeptemberCheckbox.text.toString())
        if (mOctoberCheckbox.isChecked) months = addString(months, mOctoberCheckbox.text.toString())
        if (mNovemberCheckbox.isChecked) months =
            addString(months, mNovemberCheckbox.text.toString())
        if (mDecemberCheckbox.isChecked) months =
            addString(months, mDecemberCheckbox.text.toString())

        mMushroom?.mMonth = months
    }

    private fun addString(main: String, added: String): String {
        if (main.isEmpty()) return added
        else return "$main;$added"
    }

    private fun updateMushroomImage() {
        if (mPhotoFile == null) {
            mMushroomImage.setImageDrawable(null)
        } else {
            val bitmap = PictureUtils.getScaledBitmap(mPhotoFile.path, activity!!)
            mMushroomImage.setImageBitmap(bitmap)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_PHOTO) {
            val uri = FileProvider.getUriForFile(context!!, FILE_PROVIDER, mPhotoFile)
            activity?.revokeUriPermission(uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
            updateMushroomImage()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.mushroom_fragment, menu)

        val editMushroom = menu.findItem(R.id.edit_mushroom) as MenuItem
        if (mEdit) editMushroom.setTitle(R.string.save_mushroom)
        else editMushroom.setTitle(R.string.edit_mushroom)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.edit_mushroom -> {
//                val intent = MushroomEditActivity.newIntent(context!!, mMushroom?.mId!!)
//                startActivity(intent)
                mEdit = !mEdit
                activity?.invalidateOptionsMenu()
                enableCheckbox(mEdit)
                return true
            }
            R.id.delete_mushroom -> {
                MushroomData.get(context!!).deleteMushroom(mMushroom!!)
                activity?.finish()
                return true
            }
            else -> {
                return super.onOptionsItemSelected(item)
            }
        }
    }

    companion object {

        private const val ARG_MUSHROOM_ID = "com.example.mushroompickercalendar.mushroom_id"
        private const val ARG_RECYCLER_VIEW =
            "com.example.mushroompickercalendar.from_recycler_view"
        private const val REQUEST_PHOTO = 2
        private const val FILE_PROVIDER = "com.example.mushroompickercalendar.fileprovider"

        fun newInstance(mushroomId: UUID, fromRecyclerView: Boolean): MushroomFragment {
            val args = Bundle()
            args.putSerializable(ARG_MUSHROOM_ID, mushroomId)
            args.putSerializable(ARG_RECYCLER_VIEW, fromRecyclerView)

            val fragment = MushroomFragment()
            fragment.arguments = args
            return fragment
        }
    }

}

