package com.example.core

import android.content.Context
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path
import java.io.Serializable

interface TestApi {

    @POST("webchat/{path}/set-user-subscription")
    fun test(
        @Body body: BodyTest,
        @Path("namespace") path : String
    ) : Call<ResultTest>
}

class BodyTest(
    val uuid: String
): Serializable

class ResultTest(
    val result: Boolean
): Serializable





class TestStorage(
    private val testApi: TestApi
): Storage<ResultTest>() {

    override fun getDataFromRemote(context: Context, args: Map<String, Any>): ResultTest? {
        val body = (args["body"] as? BodyTest) ?: return null
        val path = (args["path"] as? String) ?: return null
        return testApi.test(body, path).toData()
    }
}

class TestRepository(
    private val testStorage: TestStorage
) {

    fun getTestData(context: Context): ResultTest? {
        return testStorage.getData(
            context,
            null,
            ResultTest::class.java,
            mapOf(
                "body" to BodyTest("1"),
                "path" to "2"
            )
        )
    }
}


//class MainActivity : AppCompatActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
//
//        TestRepository(TestStorage()).getTestData()
//    }
//}
