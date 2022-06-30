package mattie.freelancer.reaader.model

import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.PropertyName
import java.sql.Timestamp

data class MBook(
    @Exclude  // here we add the exclude annotation to prevent fireStore from adding this field
    var id: String? = null,  // this is because fireStore already adds its own id on storage
    var title: String? = null,
    var authors: String? = null,
    var notes: String? = null,
    @get:PropertyName("book_photo_url")
    @set:PropertyName("book_photo_url")
    var photoUrl: String? = null,
    var categories: String? = null,

    @get:PropertyName("published_date")  // here we adding name format for fireStore to work fine
    @set:PropertyName("published_date")  // fireStore fields can be saved in camel case
    var publishedDate: String? = null,
    var rating: String? = null,
    var description: String? = null,

    @get:PropertyName("page_count")
    @set:PropertyName("page_count")
    var pageCount: String? = null,

    @get:PropertyName("started_reading")
    @set:PropertyName("started_reading")
    var startedReading: Timestamp? = null,

    @get:PropertyName("finished_reading")
    @set:PropertyName("finished_reading")
    var finishedReading: Timestamp? = null,

    @get:PropertyName("user_id")
    @set:PropertyName("user_id")
    var userId: String? = null,

    @get:PropertyName("google_book_id")
    @set:PropertyName("google_book_id")
    var googleBookId: String? = null
)
