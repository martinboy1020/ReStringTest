package com.example.restringtest

import android.annotation.TargetApi
import android.app.Dialog
import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.restringtest.Constants.LANGUAGE.APP_LANGUAGE
import com.example.restringtest.Constants.LANGUAGE.COUNTRY_CODE_CN
import com.example.restringtest.Constants.LANGUAGE.COUNTRY_CODE_HK
import com.example.restringtest.Constants.LANGUAGE.COUNTRY_CODE_MO
import com.example.restringtest.Constants.LANGUAGE.COUNTRY_CODE_TW
import com.example.restringtest.Constants.LANGUAGE.ENGLISH
import com.example.restringtest.Constants.LANGUAGE.LANGUAGE_CODE_CHINESE
import com.example.restringtest.Constants.LANGUAGE.LANGUAGE_CODE_ENGLISH
import com.example.restringtest.Constants.LANGUAGE.SIMPLIFIED_CHINESE
import com.example.restringtest.Constants.LANGUAGE.TRADITIONAL_CHINESE
import com.example.restringtest.databinding.DialogChangeLanguageBinding
import dev.b3nedikt.restring.Restring
import java.util.*

object LanguageUtils {

    private var changeLanguageDialogFragment: ChangeLanguageDialogFragment? = null

    /**
     * 更新Context
     */
    fun getAttachBaseContext(context: Context): Context {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            updateResources(context)
        } else {
            //7.0之前更新語言資源的方式
            changeResLanguage(context)
        }
    }

    /**
     * 語言轉換For 7.0以上(切換語言用)
     */
    @TargetApi(Build.VERSION_CODES.N)
    private fun updateResources(context: Context): Context {
        val resources: Resources = context.resources
        val locale: Locale = getCurrentLocale(context)
        val configuration: Configuration = resources.configuration
        configuration.setLocale(locale)
        Restring.locale = locale
        return context.createConfigurationContext(configuration)
    }

    /**
     * 語言轉換For 7.0以下(切換語言用)
     */
    private fun changeResLanguage(context: Context): Context {
        val resources: Resources = context.resources
        val configuration: Configuration = resources.configuration
        val locale: Locale = getCurrentLocale(context)
        configuration.locale = locale
        resources.updateConfiguration(configuration, resources.displayMetrics)
        Restring.locale = locale
        return context
    }

    /**
     * 判斷BuildConfig.LANGUAGE設定語言的數目決定如何指定語言
     * 如果超過一種語言就判斷系統語系或已存取的語系
     * 只有一種語言就設定該特定語言
     * 可用於像是SimpleDateFormat需要指定Locale時使用
     */
    fun getCurrentLocale(context: Context): Locale {
        val languageList = BuildConfig.LANGUAGE.toMutableList()

        // 如果已上線版本途中將Gradle的支援語系刪除, 但使用者已經有透過APP語系切換到不支援的語系, 就將Preference的語系清掉
        if (Preference.getString(context, APP_LANGUAGE, "") != "") {
            val language = when (Preference.getString(context, APP_LANGUAGE, "")) {
                TRADITIONAL_CHINESE -> "TRADITIONAL_CHINESE"
                SIMPLIFIED_CHINESE -> "SIMPLIFIED_CHINESE"
                ENGLISH -> "ENGLISH"
                else -> "SIMPLIFIED_CHINESE"
            }
            if (!languageList.any { it == language }) Preference.removeString(context, APP_LANGUAGE)
        }

        when {
            languageList.size > 1 -> {
                return if (Preference.getString(context, APP_LANGUAGE, "") != "") {
                    covertLanguageToLocale(Preference.getString(context, APP_LANGUAGE, ""))
                } else {
                    covertLanguageToLocale(getCurrentLocaleLanguage(context))
                }
            }
            languageList.size == 1 -> {
                return when (languageList[0]) {
                    "SIMPLIFIED_CHINESE" -> covertLanguageToLocale(SIMPLIFIED_CHINESE)
                    "TRADITIONAL_CHINESE" -> covertLanguageToLocale(TRADITIONAL_CHINESE)
                    "ENGLISH" -> covertLanguageToLocale(ENGLISH)
                    else -> covertLanguageToLocale(SIMPLIFIED_CHINESE)
                }
            }
            else -> {
                return covertLanguageToLocale(SIMPLIFIED_CHINESE)
            }
        }
    }

    /**
     * 確認當前"系統"的Locale語系
     * 7.0版本之後 Locale判斷語系需要同時判斷使用語言和國家
     */
    fun getCurrentLocaleLanguage(context: Context): String {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Log.d(
                "Language",
                "context.resources.configuration.locales[0]: " + context.resources.configuration.locales[0]
            )
            covertLocaleToLanguage(context.resources.configuration.locales[0])
        } else {
            covertLocaleToLanguage(context.resources.configuration.locale)
        }
    }

    /**
     * 判斷語言決定將Locale指向何種語言Resource
     * 繁體中文強制指向zh-rHK資源
     */
    private fun covertLanguageToLocale(languageCode: String?): Locale {
        return when (languageCode?.let { checkLocaleIsSupportLanguage(it) }) {
            TRADITIONAL_CHINESE -> Locale(LANGUAGE_CODE_CHINESE, COUNTRY_CODE_HK)
            SIMPLIFIED_CHINESE -> Locale.SIMPLIFIED_CHINESE
            ENGLISH -> Locale.ENGLISH
            else -> Locale.SIMPLIFIED_CHINESE
        }
    }

    /**
     * 檢查當前手機的Locale是否為App支援的語系
     */
    private fun checkLocaleIsSupportLanguage(languageCode: String): String {
        val languageList = BuildConfig.LANGUAGE.toMutableList()
        val language = when (languageCode) {
            TRADITIONAL_CHINESE -> "TRADITIONAL_CHINESE"
            SIMPLIFIED_CHINESE -> "SIMPLIFIED_CHINESE"
            ENGLISH -> "ENGLISH"
            else -> "SIMPLIFIED_CHINESE"
        }
        return if (languageList.any { it == language }) {
            languageCode
        } else {
            when (BuildConfig.INIT_LANGUAGE) {
                "SIMPLIFIED_CHINESE" -> SIMPLIFIED_CHINESE
                "TRADITIONAL_CHINESE" -> TRADITIONAL_CHINESE
                "ENGLISH" -> ENGLISH
                else -> SIMPLIFIED_CHINESE
            }
        }

    }

    /**
     * 判斷Locale的語言和國家決定將設定何種語言
     * 預設語言根據BuildConfig.INIT_LANGUAGE決定
     */
    private fun covertLocaleToLanguage(locale: Locale): String {
        Log.d("Language", "covertLocaleToLanguage locale: $locale")

        return when (locale.language) {
            LANGUAGE_CODE_CHINESE -> {
                // 有不同的國家都是用中文語系, 所以language會是"zh"開頭, 但簡體中文和繁體中文都是"zh"開頭所以要靠country來判斷
                when (locale.country) {
                    COUNTRY_CODE_HK -> checkLocaleIsSupportLanguage(TRADITIONAL_CHINESE)
                    COUNTRY_CODE_TW -> checkLocaleIsSupportLanguage(TRADITIONAL_CHINESE)
                    COUNTRY_CODE_MO -> checkLocaleIsSupportLanguage(TRADITIONAL_CHINESE)
                    COUNTRY_CODE_CN -> checkLocaleIsSupportLanguage(SIMPLIFIED_CHINESE)
                    else -> checkLocaleIsSupportLanguage(SIMPLIFIED_CHINESE)
                }
            }
            LANGUAGE_CODE_ENGLISH -> checkLocaleIsSupportLanguage(ENGLISH)
            else -> {
                //  如果目前的Local語系是APP不支援的, 這邊設定預設語系
                when (BuildConfig.INIT_LANGUAGE) {
                    "SIMPLIFIED_CHINESE" -> SIMPLIFIED_CHINESE
                    "TRADITIONAL_CHINESE" -> TRADITIONAL_CHINESE
                    "ENGLISH" -> ENGLISH
                    else -> SIMPLIFIED_CHINESE
                }
            }
        }
    }

    /**
     * 獲取語言字段(返回"简体中文", "繁體中文", "English"等字段)
     */
    fun getCurrentLanguageString(context: Context): String? {
        return if (Preference.getString(context, APP_LANGUAGE, "") != "") {
            Preference.getString(context, APP_LANGUAGE, "")
                ?.let { covertLanguageToLanguageString(context, it) }
        } else {
            covertLanguageToLanguageString(context, getCurrentLocaleLanguage(context))
        }
    }

    /**
     * 判斷語言決定返回語言字段
     */
    private fun covertLanguageToLanguageString(context: Context, language: String): String {
        return when (language) {
            TRADITIONAL_CHINESE -> context.resources.getString(R.string.traditional_chinese)
            SIMPLIFIED_CHINESE -> context.resources.getString(R.string.simplified_chinese)
            ENGLISH -> context.resources.getString(R.string.english)
            else -> context.resources.getString(R.string.simplified_chinese)
        }
    }

    /**
     * 獲取語言列表
     * 根據BuildConfig.Language的列表獲取languageNameArray, languageResName, languageIdArray
     * 如要修改選單上的語言顯示, 請修改gradle裡的BuildConfig.Language
     * @param isShowLanguageExceptChinese 判斷是否只顯示簡繁中(目前只有智能下單)
     */
    private fun getLanguageList(
        context: Context,
        isShowLanguageExceptChinese: Boolean? = false
    ): MutableList<LanguageData> {
        val languageList = mutableListOf<LanguageData>()
        val languageNameArray = getLanguageNameArray(context, isShowLanguageExceptChinese)
        val languageFlagResIdArray = getLanguageFlagResArray(isShowLanguageExceptChinese)
        val languageIdArray = getLanguageIdArray(isShowLanguageExceptChinese)
        if (languageNameArray.size == languageFlagResIdArray.size
            && languageNameArray.size == languageIdArray.size
            && languageFlagResIdArray.size == languageIdArray.size
        ) {
            for (i in languageNameArray.indices) {
                languageList.add(
                    LanguageData(
                        languageFlagResIdArray[i], languageIdArray[i], languageNameArray[i]
                    )
                )
            }
        }
        return languageList
    }

    /**
     * 根據BuildConfig.Language獲取語言名稱列表
     * @param isShowLanguageExceptChinese 判斷是否只顯示簡繁中
     */
    private fun getLanguageNameArray(
        context: Context,
        isShowLanguageExceptChinese: Boolean?
    ): MutableList<String> {
        val languageList = BuildConfig.LANGUAGE
        val languageNameList = mutableListOf<String>()
        if (isShowLanguageExceptChinese == true) {
            languageNameList.add(context.resources.getString(R.string.simplified_chinese))
            languageNameList.add(context.resources.getString(R.string.traditional_chinese))
        } else {
            languageList.forEach { language ->
                when (language) {
                    "SIMPLIFIED_CHINESE" -> languageNameList.add(context.resources.getString(R.string.simplified_chinese))
                    "TRADITIONAL_CHINESE" -> languageNameList.add(context.resources.getString(R.string.traditional_chinese))
                    "ENGLISH" -> languageNameList.add(context.resources.getString(R.string.english))
                }
            }
        }
        return languageNameList
    }

    /**
     * 根據BuildConfig.Language獲取語言圖片列表
     * @param isShowLanguageExceptChinese 判斷是否只顯示簡繁中
     */
    private fun getLanguageFlagResArray(isShowLanguageExceptChinese: Boolean?): MutableList<Int> {
        val languageList = BuildConfig.LANGUAGE
        val languageResList = mutableListOf<Int>()
        if (isShowLanguageExceptChinese == true) {
            languageResList.add(R.drawable.ic_flag_china)
            languageResList.add(R.drawable.ic_flag_hong_kong)
        } else {
            languageList.forEach { language ->
                when (language) {
                    "SIMPLIFIED_CHINESE" -> languageResList.add(R.drawable.ic_flag_china)
                    "TRADITIONAL_CHINESE" -> languageResList.add(R.drawable.ic_flag_hong_kong)
                    "ENGLISH" -> languageResList.add(R.drawable.ic_flag_usa)
                }
            }
        }
        return languageResList
    }

    /**
     * 根據BuildConfig.Language獲取語言ID列表
     * @param isShowLanguageExceptChinese 判斷是否只顯示簡繁中
     */
    private fun getLanguageIdArray(isShowLanguageExceptChinese: Boolean?): MutableList<String> {
        val languageList = BuildConfig.LANGUAGE
        val languageIdList = mutableListOf<String>()
        if (isShowLanguageExceptChinese == true) {
            languageIdList.add(SIMPLIFIED_CHINESE)
            languageIdList.add(TRADITIONAL_CHINESE)
        } else {
            languageList.forEach { language ->
                when (language) {
                    "SIMPLIFIED_CHINESE" -> languageIdList.add(SIMPLIFIED_CHINESE)
                    "TRADITIONAL_CHINESE" -> languageIdList.add(TRADITIONAL_CHINESE)
                    "ENGLISH" -> languageIdList.add(ENGLISH)
                }
            }
        }
        return languageIdList
    }

    data class LanguageData(val flagResId: Int, val languageId: String, val languageName: String)

    /**
     * 顯示語言列表對話框
     * @param isShowLanguageExceptChinese 除了中文以外的語言也要顯示(因應智能下單中文以外的語言不顯示的狀況)
     */
    fun showChangeLanguageDialog(
        context: Context,
        fragmentManager: FragmentManager,
        listener: ChangeLanguageDialogFragment.ChangeLanguageDialogListener? = null,
        isShowLanguageExceptChinese: Boolean? = false
    ) {
        if (changeLanguageDialogFragment?.dialog?.isShowing == true) return
        val languageList = getLanguageList(context, isShowLanguageExceptChinese)
        changeLanguageDialogFragment = ChangeLanguageDialogFragment(languageList, listener)
        changeLanguageDialogFragment?.show(
            fragmentManager,
            ChangeLanguageDialogFragment::class.java.simpleName
        )
    }

    /**
     * 關閉語言列表對話框
     */
    fun dismissChangeLanguageDialog() {
        changeLanguageDialogFragment?.dialog?.dismiss()
        changeLanguageDialogFragment = null
    }

    /**
     * 選擇語言列表對話框
     */
    class ChangeLanguageDialogFragment(
        private val languageList: MutableList<LanguageData>,
        private val listener: ChangeLanguageDialogListener? = null
    ) : BaseViewBindingDialogFragment<DialogChangeLanguageBinding>(R.layout.dialog_change_language) {

        interface ChangeLanguageDialogListener {
            fun changeLanguage(languageId: String)
        }

        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
            val dialog = Dialog(requireActivity())
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.setCancelable(true)
            dialog.setCanceledOnTouchOutside(true)
            return dialog
        }

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)
            val rvMultiLanguage = view.findViewById<RecyclerView>(R.id.rv_multi_language)
            val layoutManager = LinearLayoutManager(context)
            val adapter = MultiLanguageAdapter(
                languageList,
                object : MultiLanguageAdapter.MultiLanguageListener {
                    override fun chooseLanguage(languageId: String) {
                        dismissChangeLanguageDialog()
                        listener?.changeLanguage(languageId)
                    }
                })
            layoutManager.orientation = LinearLayoutManager.VERTICAL
            rvMultiLanguage.layoutManager = layoutManager
            rvMultiLanguage.adapter = adapter
        }

        override fun initBinding(view: View): DialogChangeLanguageBinding =
            DialogChangeLanguageBinding.bind(view)

    }

    class MultiLanguageAdapter(
        private val languageList: MutableList<LanguageData>,
        private val listener: MultiLanguageListener
    ) : RecyclerView.Adapter<MultiLanguageAdapter.MultiLanguageViewHolder>() {

        interface MultiLanguageListener {
            fun chooseLanguage(languageId: String)
        }

        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): MultiLanguageViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            return MultiLanguageViewHolder(
                inflater.inflate(
                    R.layout.item_rv_dialog_language,
                    parent,
                    false
                )
            )
        }

        override fun getItemCount(): Int {
            return languageList.size
        }

        override fun onBindViewHolder(holder: MultiLanguageViewHolder, position: Int) {
            holder.countryFlag.setBackgroundResource(languageList[position].flagResId)
            holder.languageName.text = languageList[position].languageName
            holder.itemLayout.setOnClickListener {
                listener.chooseLanguage(languageList[position].languageId)
            }
        }

        class MultiLanguageViewHolder(root: View) : RecyclerView.ViewHolder(root) {
            var itemLayout: ConstraintLayout = root.findViewById(R.id.layout_language_item)
            var countryFlag: ImageView = root.findViewById(R.id.img_country_flag)
            var languageName: TextView = root.findViewById(R.id.tv_language)
        }

    }

}