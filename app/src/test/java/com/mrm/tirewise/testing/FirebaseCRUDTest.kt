package com.mrm.tirewise.testing

import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.TaskCompletionSource
import com.google.common.base.Verify.verify
import com.google.firebase.Timestamp
import com.google.firebase.Timestamp.now
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.util.Executors
import com.google.firebase.firestore.util.Util.voidErrorTransformer
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.every
import org.junit.Assert.*
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.spyk
import io.mockk.unmockkStatic
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.runner.RunWith
import java.util.Date

class FirebaseCRUDTest{


    val path = "vehicles"
    val documentId = "f46c3d0f-b1fc-49f9-8501-63b07b12b4a1"
    val document = mutableMapOf<String, Any>(
        "Id" to documentId,
        "imageUri" to "",
        "plateNo" to "298681",
        "userId" to "FH4IpZRu32YAZJB7VCsAeYdMoyB3",
        "vehicleColor" to "Red",
        "vehicleMake" to "Toyota",
        "vehicleModel" to "Camry",
        "vehicleName" to "Ridhwan's Car",
        "yearMake" to "2023",

    )

    inline fun <reified T> mockTask(result: T?, exception: Exception? = null): Task<T> {
        val task: Task<T> = mockk(relaxed = true)
        every { task.isComplete } returns true
        every { task.exception } returns exception
        every { task.isCanceled } returns false
        val relaxedT: T = mockk(relaxed = true)
        every { task.result } returns result
        return task
    }

    @Test
    fun `Testing createDocument()`() {

        // calling Timestamp.now()
        mockkStatic(::now)
        every { now() } returns Timestamp(Date(0))

        val taskCompletionSource = TaskCompletionSource<Void>()

        val mockdb: FirebaseFirestore = mockk {
            every {
                collection(path)
                    .document(document.get("Id").toString())
                    .set(document)
            } returns taskCompletionSource.task.continueWith(Executors.DIRECT_EXECUTOR, voidErrorTransformer())
        }

        val firebaseCrud = spyk(FirebaseCRUD.FirebaseCRUD(mockdb), recordPrivateCalls = true)

        runBlocking {
            firebaseCrud.createDocument(document)

            coVerify(exactly = 1) {
                firebaseCrud.createDocument(document)
            }
            confirmVerified(firebaseCrud)

            verify(exactly = 1) {
                mockdb
                    .collection(path)
                    .document(document.get("Id").toString())
                    .set(document)
            }
            confirmVerified(mockdb)


            verify(exactly = 0) {
                Timestamp.now()
            }
        }

        //  Don't forget to unmock.
        unmockkStatic(::now)
    }


    @Test
    fun `Testing readDocument()`() {
        val documentSnapshot = mockk<DocumentSnapshot> {
            every { id } returns documentId
            every { data } returns document
        }

        val mockdb: FirebaseFirestore = mockk {
            every {
                document("$path/$documentId")
                    .get()

            } returns mockTask<DocumentSnapshot>(documentSnapshot)
        }

        val firebaseCrud = spyk(FirebaseCRUD.FirebaseCRUD(mockdb), recordPrivateCalls = true)

        runBlocking {
            val plateNo = firebaseCrud.readDocument(documentId, "plateNo")

            assertEquals(document.get("plateNo"), plateNo)

            coVerify(exactly = 1) {
                firebaseCrud.readDocument(documentId, "plateNo")
            }
            confirmVerified(firebaseCrud)

            verify(exactly = 1) {
                mockdb.document("$path/$documentId")
                    .get()
            }
            confirmVerified(mockdb)
        }
    }

    @Test
    fun `Testing updateDocument()`() {
        val property = "plateNo"
        val propertyValue = "974192"

        val taskCompletionSource = TaskCompletionSource<Void>()

        val mockdb: FirebaseFirestore = mockk {
            every {
                document("$path/$documentId")
                    .update(property, propertyValue)

            } returns taskCompletionSource.task.continueWith(com.google.firebase.firestore.util.Executors.DIRECT_EXECUTOR, voidErrorTransformer())
        }

        val firebaseCrud = spyk(FirebaseCRUD.FirebaseCRUD(mockdb), recordPrivateCalls = true)

        runBlocking {
            firebaseCrud.updateDocument(documentId, property, propertyValue)

            coVerify(exactly = 1) {
                firebaseCrud.updateDocument(documentId, property, propertyValue)
            }
            confirmVerified(firebaseCrud)

            verify(exactly = 1) {
                mockdb.document("$path/$documentId")
                    .update(property, propertyValue)
            }
            confirmVerified(mockdb)
        }
    }



    @Test
    fun `Testing deleteDocument()`() {
        val taskCompletionSource = TaskCompletionSource<Void>()

        val mockdb: FirebaseFirestore = mockk {
            every {
                document("$path/$documentId")
                    .delete()
            } returns taskCompletionSource.task.continueWith(Executors.DIRECT_EXECUTOR, voidErrorTransformer())
        }

        val firebaseCrud = spyk(FirebaseCRUD.FirebaseCRUD(mockdb), recordPrivateCalls = true)

        runBlocking {
            firebaseCrud.deleteDocument(documentId)

            coVerify(exactly = 1) {
                firebaseCrud.deleteDocument(documentId)
            }
            confirmVerified(firebaseCrud)

            verify(exactly = 1) {
                mockdb.
                document("$path/$documentId")
                    .delete()
            }
            confirmVerified(mockdb)
        }
    }

}