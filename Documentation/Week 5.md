Last part of Documentation.


UserEventAdapter:
The UserEventAdapter class is responsible for displaying events for users and allowing them to join events.

Initializing Adapter:
class UserEventAdapter(private val events: List<EventModel>, private val activity: UserHomeActivity) :
    RecyclerView.Adapter<UserEventAdapter.EventViewHolder>() {



Binding Data and Handling Event Join:
    binding.btnJoin1.text = if (event.isJoined) "Joined" else "Join"
    binding.btnJoin1.isEnabled = !event.isJoined

    binding.btnJoin1.setOnClickListener {
        joinEvent(event, binding)
    }

Joining an Event
private fun joinEvent(event: EventModel, binding: ItemEventBinding) {
    val firestore = FirebaseFirestore.getInstance()
    firestore.collection("events").document(event.id!!)
        .update("isJoined", true)
        .addOnSuccessListener {
            event.isJoined = true
            binding.btnJoin1.text = "Joined"
            binding.btnJoin1.isEnabled = false
        }
}

