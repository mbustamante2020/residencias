package com.residencias.es.ui.advertising

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.paypal.android.sdk.payments.*
import com.residencias.es.R
import com.residencias.es.databinding.FragmentAdvertisingBinding
import org.json.JSONException
import java.math.BigDecimal
import java.util.*
import kotlin.collections.HashSet

open class AdvertisingFragment : Fragment(R.layout.fragment_advertising) {

    private var _binding: FragmentAdvertisingBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentAdvertisingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(itemView: View, savedInstanceState: Bundle?) {
        super.onViewCreated(itemView, savedInstanceState)
    }

    fun onBuyPressed(pressed: View) {
        /*
         * PAYMENT_INTENT_SALE will cause the payment to complete immediately.
         * Change PAYMENT_INTENT_SALE to
         *   - PAYMENT_INTENT_AUTHORIZE to only authorize payment and capture funds later.
         *   - PAYMENT_INTENT_ORDER to create a payment for authorization and capture
         *     later via calls from your server.
         *
         * Also, to include additional payment details and an item list, see getStuffToBuy() below.
         */
        val thingToBuy = getThingToBuy(PayPalPayment.PAYMENT_INTENT_SALE)

        /*
         * See getStuffToBuy(..) for examples of some available payment options.
         */

        val intent = Intent(context, PaymentActivity::class.java)

        // send the same configuration for restart resiliency
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config)

        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, thingToBuy)

        startActivityForResult(intent, REQUEST_CODE_PAYMENT)
    }

    private fun getThingToBuy(paymentIntent: String): PayPalPayment {
        return PayPalPayment(BigDecimal("1.75"), "USD", "sample item",
                paymentIntent)
    }

    /*
     * This method shows use of optional payment details and item list.
     */
    private fun getStuffToBuy(paymentIntent: String): PayPalPayment {
        //--- include an item list, payment amount details
        val items = arrayOf(
                PayPalItem("sample item #1", 2, BigDecimal("87.50"), "USD", "sku-12345678"),
                PayPalItem("free sample item #2", 1, BigDecimal("0.00"), "USD", "sku-zero-price"),
                PayPalItem("sample item #3 with a longer name", 6, BigDecimal("37.99"), "USD", "sku-33333")
        )
        val subtotal = PayPalItem.getItemTotal(items)
        val shipping = BigDecimal("7.21")
        val tax = BigDecimal("4.67")
        val paymentDetails = PayPalPaymentDetails(shipping, subtotal, tax)
        val amount = subtotal.add(shipping).add(tax)
        val payment = PayPalPayment(amount, "USD", "sample item", paymentIntent)
        payment.items(items).paymentDetails(paymentDetails)

        //--- set other optional fields like invoice_number, custom field, and soft_descriptor
        payment.custom("This is text that will be associated with the payment that the app can use.")

        return payment
    }

    /*
     * Add app-provided shipping address to payment
     */
    private fun addAppProvidedShippingAddress(paypalPayment: PayPalPayment) {
        val shippingAddress = ShippingAddress()
                .recipientName("Mom Parker")
                .line1("52 North Main St.")
                .city("Austin")
                .state("TX")
                .postalCode("78729")
                .countryCode("US")
        paypalPayment.providedShippingAddress(shippingAddress)
    }

    /*
     * Enable retrieval of shipping addresses from buyer's PayPal account
     */
    private fun enableShippingAddressRetrieval(paypalPayment: PayPalPayment, enable: Boolean) {
        paypalPayment.enablePayPalShippingAddressesRetrieval(enable)
    }

    fun onFuturePaymentPressed(pressed: View) {
    /*    val intent = Intent(FragmentAdvertisingBinding, PayPalFuturePaymentActivity::class.java)

        // send the same configuration for restart resiliency
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config)

        startActivityForResult(intent, REQUEST_CODE_FUTURE_PAYMENT)*/
    }

    fun onProfileSharingPressed(pressed: View) {
      /*  val intent = Intent(this@SampleActivity, PayPalProfileSharingActivity::class.java)

        // send the same configuration for restart resiliency
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config)

        intent.putExtra(PayPalProfileSharingActivity.EXTRA_REQUESTED_SCOPES, oauthScopes)

        startActivityForResult(intent, REQUEST_CODE_PROFILE_SHARING)*/
    }

    private /* create the set of required scopes
         * Note: see https://developer.paypal.com/docs/integration/direct/identity/attributes/ for mapping between the
         * attributes you select for this app in the PayPal developer portal and the scopes required here.
         */ val oauthScopes: PayPalOAuthScopes
        get() {
            val scopes = HashSet(
                    Arrays.asList(
                            PayPalOAuthScopes.PAYPAL_SCOPE_EMAIL,
                            PayPalOAuthScopes.PAYPAL_SCOPE_ADDRESS
                    )
            )
            return PayPalOAuthScopes(scopes)
        }

    protected fun displayResultText(result: String) {
        binding.txtResult.setText("Result : $result")

        Toast.makeText(
                context,
                result, Toast.LENGTH_LONG).show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_CODE_PAYMENT) {
            if (resultCode == Activity.RESULT_OK) {
                val confirm = data?.getParcelableExtra<PaymentConfirmation>(PaymentActivity.EXTRA_RESULT_CONFIRMATION)
                if (confirm != null) {
                    try {
                        Log.i(TAG, confirm.toJSONObject().toString(4))
                        Log.i(TAG, confirm.payment.toJSONObject().toString(4))
                        /**
                         * TODO: send 'confirm' (and possibly confirm.getPayment() to your server for verification
                         * or consent completion.
                         * See https://developer.paypal.com/webapps/developer/docs/integration/mobile/verify-mobile-payment/
                         * for more details.
                         * For sample mobile backend interactions, see
                         * https://github.com/paypal/rest-api-sdk-python/tree/master/samples/mobile_backend
                         */
                        displayResultText("PaymentConfirmation info received from PayPal")


                    } catch (e: JSONException) {
                        Log.e(TAG, "an extremely unlikely failure occurred: ", e)
                    }

                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Log.i(TAG, "The user canceled.")
            } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
                Log.i(
                        TAG,
                        "An invalid Payment or PayPalConfiguration was submitted. Please see the docs.")
            }
        } else if (requestCode == REQUEST_CODE_FUTURE_PAYMENT) {
            if (resultCode == Activity.RESULT_OK) {
                val auth = data?.getParcelableExtra<PayPalAuthorization>(PayPalFuturePaymentActivity.EXTRA_RESULT_AUTHORIZATION)
                if (auth != null) {
                    try {
                        Log.i("FuturePaymentExample", auth.toJSONObject().toString(4))

                        val authorizationCode = auth.authorizationCode
                        Log.i("FuturePaymentExample", authorizationCode)

                        sendAuthorizationToServer(auth)
                        displayResultText("Future Payment code received from PayPal")

                    } catch (e: JSONException) {
                        Log.e("FuturePaymentExample", "an extremely unlikely failure occurred: ", e)
                    }

                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Log.i("FuturePaymentExample", "The user canceled.")
            } else if (resultCode == PayPalFuturePaymentActivity.RESULT_EXTRAS_INVALID) {
                Log.i(
                        "FuturePaymentExample",
                        "Probably the attempt to previously start the PayPalService had an invalid PayPalConfiguration. Please see the docs.")
            }
        } else if (requestCode == REQUEST_CODE_PROFILE_SHARING) {
            if (resultCode == Activity.RESULT_OK) {
                val auth = data?.getParcelableExtra<PayPalAuthorization>(PayPalProfileSharingActivity.EXTRA_RESULT_AUTHORIZATION)
                if (auth != null) {
                    try {
                        Log.i("ProfileSharingExample", auth.toJSONObject().toString(4))

                        val authorizationCode = auth.authorizationCode
                        Log.i("ProfileSharingExample", authorizationCode)

                        sendAuthorizationToServer(auth)
                        displayResultText("Profile Sharing code received from PayPal")

                    } catch (e: JSONException) {
                        Log.e("ProfileSharingExample", "an extremely unlikely failure occurred: ", e)
                    }

                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Log.i("ProfileSharingExample", "The user canceled.")
            } else if (resultCode == PayPalFuturePaymentActivity.RESULT_EXTRAS_INVALID) {
                Log.i(
                        "ProfileSharingExample",
                        "Probably the attempt to previously start the PayPalService had an invalid PayPalConfiguration. Please see the docs.")
            }
        }
    }

    private fun sendAuthorizationToServer(authorization: PayPalAuthorization) {

        /**
         * TODO: Send the authorization response to your server, where it can
         * exchange the authorization code for OAuth access and refresh tokens.
         * Your server must then store these tokens, so that your server code
         * can execute payments for this user in the future.
         * A more complete example that includes the required app-server to
         * PayPal-server integration is available from
         * https://github.com/paypal/rest-api-sdk-python/tree/master/samples/mobile_backend
         */

    }

    fun onFuturePaymentPurchasePressed(pressed: View) {
        // Get the Client Metadata ID from the SDK
        val metadataId = PayPalConfiguration.getClientMetadataId(context)

        Log.i("FuturePaymentExample", "Client Metadata ID: $metadataId")

        // TODO: Send metadataId and transaction details to your server for processing with
        // PayPal...
        displayResultText("Client Metadata Id received from SDK")
    }

    public override fun onDestroy() {
        // Stop service when done
        //stopService(Intent(context, PayPalService::class.java))
        super.onDestroy()
    }

    companion object {
        private const val TAG = "paymentExample"
        /**
         * - Set to PayPalConfiguration.ENVIRONMENT_PRODUCTION to move real money.
         * - Set to PayPalConfiguration.ENVIRONMENT_SANDBOX to use your test credentials
         * from https://developer.paypal.com
         * - Set to PayPalConfiguration.ENVIRONMENT_NO_NETWORK to kick the tires
         * without communicating to PayPal's servers.
         */
        private const val CONFIG_ENVIRONMENT = PayPalConfiguration.ENVIRONMENT_NO_NETWORK

        // note that these credentials will differ between live & sandbox environments.
        private const val CONFIG_CLIENT_ID = "credentials from developer.paypal.com"

        private const val REQUEST_CODE_PAYMENT = 1
        private const val REQUEST_CODE_FUTURE_PAYMENT = 2
        private const val REQUEST_CODE_PROFILE_SHARING = 3

        private val config = PayPalConfiguration()
                .environment(CONFIG_ENVIRONMENT)
                .clientId(CONFIG_CLIENT_ID)
                .merchantName("Example Merchant")
                .merchantPrivacyPolicyUri(Uri.parse("https://www.example.com/privacy"))
                .merchantUserAgreementUri(Uri.parse("https://www.example.com/legal"))
    }
}
