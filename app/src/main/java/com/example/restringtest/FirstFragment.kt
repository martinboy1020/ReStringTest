package com.example.restringtest

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.example.restringtest.LanguageUtils.ChangeLanguageDialogFragment.ChangeLanguageDialogListener
import com.example.restringtest.LanguageUtils.getCurrentLocaleLanguage
import com.example.restringtest.LanguageUtils.showChangeLanguageDialog
import com.example.restringtest.Preference.saveString
import com.example.restringtest.databinding.FragmentFirstBinding

class FirstFragment : BaseViewBindingFragment<FragmentFirstBinding>(R.layout.fragment_first) {

//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        return inflater.inflate(R.layout.fragment_first, container, false)
//    }
    override fun initBinding(view: View): FragmentFirstBinding = FragmentFirstBinding.bind(view)

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        val btnActivityB: Button = view.findViewById(R.id.btn_activity_b)
//        val tvQuantity: TextView = view.findViewById(R.id.tv_quantity)

        binding.tvQuantity.text = resources.getQuantityString(
            R.plurals.quantity_string,
            1,
            1
        ) + "\n" + resources.getQuantityString(R.plurals.quantity_string, 2, 2)

        binding.btnActivityB.setOnClickListener {
            showChangeLanguageDialog(
                requireContext(), parentFragmentManager,
                object : ChangeLanguageDialogListener {
                    override fun changeLanguage(languageId: String) {
                        val settingLanguage =
                            Preference.getString(
                                requireContext(),
                                Constants.LANGUAGE.APP_LANGUAGE,
                                ""
                            )
                        if (settingLanguage == "") {
                            val mobileDefaultLanguage =
                                getCurrentLocaleLanguage(requireContext())
                            if (mobileDefaultLanguage != languageId) {
                                saveString(
                                    requireContext(),
                                    Constants.LANGUAGE.APP_LANGUAGE,
                                    languageId
                                )
                                val intent = Intent(requireContext(), SplashActivity::class.java)
                                intent.flags =
                                    Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                startActivity(intent)
                            }
                        } else {
                            if (settingLanguage != languageId) {
                                saveString(
                                    requireContext(),
                                    Constants.LANGUAGE.APP_LANGUAGE,
                                    languageId
                                )
                                val intent = Intent(requireContext(), SplashActivity::class.java)
                                intent.flags =
                                    Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                startActivity(intent)
                            }
                        }
                    }
                }, false
            )
        }

    }

}