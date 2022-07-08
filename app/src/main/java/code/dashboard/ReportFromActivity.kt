package code.dashboard

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import code.utils.AppSettings
import code.utils.AppUrls
import code.utils.AppUtils
import code.utils.OSettings
import code.view.BaseActivity
import com.amtz.v2home.R
import com.amtz.v2home.databinding.ActivityReportFromBinding
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import org.json.JSONException
import org.json.JSONObject

class ReportFromActivity : BaseActivity() {

    @RequiresApi(Build.VERSION_CODES.R)
    private lateinit var binding: ActivityReportFromBinding

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReportFromBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnFirst.setOnClickListener(View.OnClickListener {
            try {
                setDefault()
                //btnDesc.setBackgroundTintList(ContextCompat.getColorStateList(mActivity, R.color.colorPrimaryDark));
                binding.btnFirst.setBackgroundColor(resources.getColor(R.color.green))
                binding.btnFirst.setTextColor(mActivity!!.resources.getColor(R.color.white))
                binding.inFirst.liFirst.visibility = View.VISIBLE
            } catch (e: Exception) {
                e.printStackTrace()
            }
        })

        binding.btnSecond.setOnClickListener(View.OnClickListener {
            try {
                setDefault()
                binding.btnSecond.setBackgroundColor(resources.getColor(R.color.green))
                binding.btnSecond.setTextColor(mActivity!!.resources.getColor(R.color.white))
                binding.inSecond.liSecond.visibility = View.VISIBLE
            } catch (e: Exception) {
                Log.v("gdbfgd", e.message.toString())
            }
        })

        binding.btnThird.setOnClickListener(View.OnClickListener {
            try {
                setDefault()
                binding.btnThird.setBackgroundColor(resources.getColor(R.color.green))
                binding.btnThird.setTextColor(mActivity!!.resources.getColor(R.color.white))
                binding.inThird.liThird.visibility = View.VISIBLE
            } catch (e: Exception) {
                Log.v("gdbfgd", e.message.toString())
            }
        })

        binding.inFirst.btnFirstNext.setOnClickListener(View.OnClickListener {
            try {
                prepareJsonForStepOne()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        })

        binding.inSecond.btnSecondNext.setOnClickListener(View.OnClickListener {
            try {
                prepareJsonForStepTwo()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        })

        binding.inThird.btnThirdNext.setOnClickListener(View.OnClickListener {
            try {
                prepareJsonForStepThree()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        })

        setDefault()
    }

    override fun onResume() {
        super.onResume()
        hitCheckSubmitApi();
    }

    @RequiresApi(Build.VERSION_CODES.R)
    fun setDefault() {

        binding.inFirst.liFirst.visibility = View.GONE
        binding.inSecond.liSecond.visibility = View.GONE
        binding.inThird.liThird.visibility = View.GONE
        binding.btnFirst.setBackgroundTintList(null)
        binding.btnSecond.setBackgroundTintList(null)
        binding.btnThird.setBackgroundTintList(null)
        binding.btnFirst.setBackgroundColor(resources.getColor(R.color.white))
        binding.btnSecond.setBackgroundColor(resources.getColor(R.color.white))
        binding.btnThird.setBackgroundColor(resources.getColor(R.color.white))
        binding.btnFirst.setTextColor(mActivity!!.resources.getColor(R.color.black))
        binding.btnSecond.setTextColor(mActivity!!.resources.getColor(R.color.black))
        binding.btnThird.setTextColor(mActivity!!.resources.getColor(R.color.black))
    }

    @RequiresApi(Build.VERSION_CODES.R)
    fun prepareJsonForStepOne() {
        try {
            val json = JSONObject()
            val jsonObject = JSONObject()
            //Chief Complaints

            jsonObject.put("asthama", binding.inFirst.etAsthma.text.toString())
            jsonObject.put("asthama_rx", binding.inFirst.etAsthmaRx.text.toString())
            jsonObject.put("diabetes", binding.inFirst.etDiabetes.text.toString())
            jsonObject.put("diabetes_rx", binding.inFirst.etDiabetesRx.text.toString())
            jsonObject.put("hypertension", binding.inFirst.etHypertension.text.toString())
            jsonObject.put("hypertension_rx", binding.inFirst.etHypertensionRx.text.toString())
            jsonObject.put("heart_disease", binding.inFirst.etHeartDisease.text.toString())
            jsonObject.put("heart_disease_rx", binding.inFirst.etHeartDiseaseRx.text.toString())
            jsonObject.put("thyroid", binding.inFirst.etThyroid.text.toString())
            jsonObject.put("thyroid_rx", binding.inFirst.etThyroidRx.text.toString())
            jsonObject.put("others", binding.inFirst.etOthers.text.toString())
            jsonObject.put("others_rx", binding.inFirst.etOthersRx.text.toString())

            //Past History

            jsonObject.put("family_history", binding.inFirst.etFamilyHistory.text.toString())
            jsonObject.put("surgical_history", binding.inFirst.etSurgicalHistory.text.toString())

            //Visula Acuity

            jsonObject.put("visula_acuity_od", binding.inFirst.etAcuityOd.text.toString())
            jsonObject.put("visula_acuity_od_ph", binding.inFirst.etAcuityPh.text.toString())
            jsonObject.put("visula_acuity_od_nv", binding.inFirst.etAcuityNv.text.toString())

            jsonObject.put("visula_acuity_os", binding.inFirst.etAcuityOs.text.toString())
            jsonObject.put("visula_acuity_os_ph", binding.inFirst.etAcuityOsPh.text.toString())
            jsonObject.put("visula_acuity_os_nv", binding.inFirst.etAcuityOsNv.text.toString())

            //PGP

            jsonObject.put("pgp_od", binding.inFirst.etPgpOd.text.toString())
            jsonObject.put("pgp_od_add", binding.inFirst.etPgpAdd.text.toString())
            jsonObject.put("pgp_od_vac_pg_od", binding.inFirst.etVacPgOd.text.toString())
            jsonObject.put("pgp_od_nv", binding.inFirst.etPgNv.text.toString())

            jsonObject.put("pgp_os", binding.inFirst.etPgpOs.text.toString())
            jsonObject.put("pgp_os_add", binding.inFirst.etPgpOsAdd.text.toString())
            jsonObject.put("pgp_os_vac_pg_os", binding.inFirst.etVacPgOs.text.toString())
            jsonObject.put("pgp_os_nv", binding.inFirst.etPgOsNv.text.toString())

            //Medical History

            jsonObject.put("allergy", binding.inFirst.etAllergy.text.toString())
            jsonObject.put("tb_syl_hiv", binding.inFirst.etTvSylHiv.text.toString())
            jsonObject.put(
                "medicine_presently",
                binding.inFirst.etMedicinePresently.text.toString()
            )
            json.put("params", jsonObject)
            Log.v("PrepareJson", json.toString())
            hitSubmitApi(AppUrls.report_step_first, json)
            //postApi(AppConstants.login, json, true)
        } catch (e: JSONException) {
            e.printStackTrace()
        }

    }

    @RequiresApi(Build.VERSION_CODES.R)
    fun prepareJsonForStepTwo() {
        try {
            val json = JSONObject()
            val jsonObject = JSONObject()
            //Chief Complaints

            jsonObject.put("retinoscopy_od", binding.inSecond.etRetinoscopyOD.text.toString())
            jsonObject.put("retinoscopy_os", binding.inSecond.etRetinoscopyOS.text.toString())

            jsonObject.put("retinoscopy_ar_od", binding.inSecond.etArOD.text.toString())
            jsonObject.put("retinoscopy_ar_os", binding.inSecond.etArOs.text.toString())

            jsonObject.put("retinoscopy_st_od", binding.inSecond.etStOd.text.toString())
            jsonObject.put("retinoscopy_st_va", binding.inSecond.etStVa.text.toString())
            jsonObject.put("retinoscopy_st_add", binding.inSecond.etStAdd.text.toString())
            jsonObject.put("retinoscopy_st_nv", binding.inSecond.etStNv.text.toString())
            jsonObject.put("retinoscopy_st_os", binding.inSecond.etStOs.text.toString())
            jsonObject.put("retinoscopy_st_os_va", binding.inSecond.etStOsVa.text.toString())
            jsonObject.put("retinoscopy_st_os_add", binding.inSecond.etStOsAdd.text.toString())
            jsonObject.put("retinoscopy_st_os_nv", binding.inSecond.etStOsNv.text.toString())

            //Past History

            jsonObject.put("dilated_ar_od", binding.inSecond.etDilatedOd.text.toString())
            jsonObject.put("dilated_ar_os", binding.inSecond.etDilatedOs.text.toString())

            //Visula Acuity

            jsonObject.put("dilated_st_od", binding.inSecond.etDilatedStOd.text.toString())
            jsonObject.put("dilated_st_va", binding.inSecond.etDilatedStVa.text.toString())
            jsonObject.put("dilated_st_os", binding.inSecond.etDilatedStOs.text.toString())
            jsonObject.put("dilated_st_os_va", binding.inSecond.etDilatedStVa.text.toString())
            jsonObject.put("dilated_hb_percent", binding.inSecond.etHbPercent.text.toString())
            jsonObject.put("dilated_f_pp_rbs", binding.inSecond.etfpprbs.text.toString())
            jsonObject.put("dilated_hbsag", binding.inSecond.etHbsag.text.toString())
            jsonObject.put("dilated_hcv", binding.inSecond.etHcv.text.toString())
            jsonObject.put("dilated_hiv", binding.inSecond.etHiv.text.toString())


            jsonObject.put("pachvmetrv_od", binding.inSecond.etPachvmetrvOd.text.toString())
            jsonObject.put("pachvmetrv_os", binding.inSecond.etPachvmetrvOs.text.toString())

            jsonObject.put("iop_od", binding.inSecond.etIopOd.text.toString())
            jsonObject.put("iop_os", binding.inSecond.etIopOs.text.toString())

            jsonObject.put("bp_od", binding.inSecond.etBp.text.toString())

            jsonObject.put("ducts_od", binding.inSecond.etDuctsOd.text.toString())
            jsonObject.put("ducts_os", binding.inSecond.etDuctsOs.text.toString())

            jsonObject.put("h_t_c_d_a", binding.inSecond.etHtcdaOd.text.toString())
            jsonObject.put("time", binding.inSecond.etTime.text.toString())

            jsonObject.put("color_vision_od", binding.inSecond.etColorVisionOd.text.toString())
            jsonObject.put("color_vision_os", binding.inSecond.etColorVisionOs.text.toString())

            json.put("params", jsonObject)
            Log.v("PrepareJson", json.toString())
            hitSubmitApi(AppUrls.report_step_second, json)
            //postApi(AppConstants.login, json, true)
        } catch (e: JSONException) {
            e.printStackTrace()
        }

    }

    @RequiresApi(Build.VERSION_CODES.R)
    fun prepareJsonForStepThree() {
        try {
            val json = JSONObject()
            val jsonObject = JSONObject()
            //Chief Complaints

            jsonObject.put("eye_lids_od", binding.inThird.etAnteeriorOd.text.toString())
            jsonObject.put("eye_lids_os", binding.inThird.etAnteeriorOd.text.toString())

            jsonObject.put("conjucative_od", binding.inThird.etConjucativeOd.text.toString())
            jsonObject.put("conjucative_os", binding.inThird.etConjucativeOs.text.toString())

            jsonObject.put("cornia_od", binding.inThird.etCorneaOd.text.toString())
            jsonObject.put("cornia_os", binding.inThird.etCorneaOs.text.toString())


            jsonObject.put(
                "anterior_chamber_od",
                binding.inThird.etAnteriorChamberOd.text.toString()
            )
            jsonObject.put(
                "anterior_chamber_os",
                binding.inThird.etAnteriorChamberOs.text.toString()
            )


            jsonObject.put("iris_od", binding.inThird.etIrisOd.text.toString())
            jsonObject.put("iris_os", binding.inThird.etIrisOs.text.toString())

            jsonObject.put("pupil_od", binding.inThird.etPupilOd.text.toString())
            jsonObject.put("pupil_os", binding.inThird.etPupilOs.text.toString())

            jsonObject.put("lens_od", binding.inThird.etLensOd.text.toString())
            jsonObject.put("lens_os", binding.inThird.etLensOs.text.toString())


            jsonObject.put("posterior_segment", binding.inThird.etPostreriorSegment.text.toString())
            jsonObject.put("funds_examination", binding.inThird.etFundusExamination.text.toString())

            jsonObject.put(
                "provisional_diagnosis",
                binding.inThird.etProvisionalDiagnosis.text.toString()
            )

            jsonObject.put("advice", binding.inThird.etAdvice.text.toString())

            json.put("params", jsonObject)
            Log.v("PrepareJson", json.toString())
            hitSubmitApi(AppUrls.report_step_third, json)
            //postApi(AppConstants.login, json, true)
        } catch (e: JSONException) {
            e.printStackTrace()
        }

    }

    private fun hitSubmitApi(url: String, jsonObject: JSONObject) {
        if (AppUtils.isNetworkAvailable(mActivity!!)) {
            AppUtils.showRequestDialog(mActivity!!)

            Log.v("auth", OSettings.getString("token").toString())

            AndroidNetworking.post(url)
                .addHeaders("auth", OSettings.getString("token"))
                .addHeaders("Content-Type", "application/json")
                .addBodyParameter("order_id", intent.getStringExtra("order_number").toString())
                .addBodyParameter("first_step_data", jsonObject.toString())
                .addBodyParameter("second_step_data", jsonObject.toString())
                .addBodyParameter("third_step_data", jsonObject.toString())
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(object : JSONObjectRequestListener {
                    @RequiresApi(Build.VERSION_CODES.R)
                    override fun onResponse(response: JSONObject) {
                        AppUtils.hideDialog()
                        try {
                            var res_od = response.getString("res_code")
                            if (res_od == "200") {

                                if (response.has("result")) {

                                    var jsonObject = response.getJSONObject("result")

                                    if (jsonObject.getString("form_steps") == "2") {
                                        binding.btnFirst.isEnabled = false
                                        binding.btnSecond.performClick()
                                    } else if (jsonObject.getString("form_steps") == "3") {
                                        binding.btnThird.performClick()
                                        binding.btnSecond.isEnabled = false
                                        binding.btnFirst.isEnabled = false
                                    } else {
                                        startActivity(
                                            Intent(
                                                mActivity,
                                                CompounderActivity::class.java
                                            )
                                        )
                                        finish()
                                    }
                                } else {
                                    Toast.makeText(
                                        mActivity,
                                        response.getString("message"),
                                        Toast.LENGTH_LONG
                                    )
                                        .show()

                                    startActivity(
                                        Intent(
                                            mActivity,
                                            CompounderActivity::class.java
                                        )
                                    )
                                    finish()
                                }
                            } else {
                                Toast.makeText(
                                    mActivity,
                                    response.getString("message"),
                                    Toast.LENGTH_LONG
                                )
                                    .show()
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()

                            Toast.makeText(
                                mActivity,
                                response.getString("message"),
                                Toast.LENGTH_LONG
                            )
                                .show()
                        }
                        Log.v("yurwe", response.toString());
                    }

                    override fun onError(anError: ANError) {
                        AppUtils.hideDialog()
                        Log.v("yurwe", anError.message.toString());
                    }
                })
        }
    }

    private fun hitCheckSubmitApi() {
        if (AppUtils.isNetworkAvailable(mActivity!!)) {
            AppUtils.showRequestDialog(mActivity!!)
            Log.v("auth", OSettings.getString("token").toString())
            AndroidNetworking.post(AppUrls.form_steps)
                .addHeaders("auth", OSettings.getString("token"))
                .addHeaders("Content-Type", "application/json")
                .addBodyParameter("order_id", intent.getStringExtra("order_number").toString())
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(object : JSONObjectRequestListener {
                    @RequiresApi(Build.VERSION_CODES.R)
                    override fun onResponse(response: JSONObject) {
                        AppUtils.hideDialog()
                        try {

                            var res_code = response.getString("res_code");

                            if (res_code == "200") {

                                var jsonObject = response.getJSONObject("result")
                                var step_count = jsonObject.getString("form_steps")
                                if (step_count == "1") {
                                    binding.btnFirst.performClick()
                                } else if (step_count == "2") {
                                    binding.btnFirst.isEnabled = false
                                    binding.btnSecond.performClick()
                                } else if (step_count == "3") {
                                    binding.btnFirst.isEnabled = false
                                    binding.btnSecond.isEnabled = false
                                    binding.btnThird.performClick()
                                } else if (step_count == "4") {
                                    Toast.makeText(
                                        mActivity,
                                        "Report is already Generated!!",
                                        Toast.LENGTH_LONG
                                    )
                                        .show()
                                    onBackPressed()
                                }

                            } else {
                                Toast.makeText(
                                    mActivity,
                                    response.getString("message"),
                                    Toast.LENGTH_LONG
                                )
                                    .show()
                            }


                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                        Log.v("gdhfsa", response.toString());
                    }

                    override fun onError(anError: ANError) {
                        AppUtils.hideDialog()
                        Log.v("gdhfsa", anError.message.toString());
                    }
                })
        }
    }

}