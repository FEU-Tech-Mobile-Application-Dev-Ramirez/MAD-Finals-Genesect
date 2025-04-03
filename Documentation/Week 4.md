Organizer Home Activity
The OrganizerHomeActivity class is responsible for managing event creation, Firestore integration, and image uploads.

Initializing Components and Loading Events:
    override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = OrganizerHomeBinding.inflate(layoutInflater)
    setContentView(binding.root)

    binding.recyclerView.layoutManager = LinearLayoutManager(this)
    eventAdapter = EventAdapter(events, this)
    binding.recyclerView.adapter = eventAdapter

    binding.fabAddEvent.setOnClickListener {
        addNewEvent()
    }

    setupImagePicker()
    loadEventsFromFirestore()
    }

Loading Events from Firestore:
    private fun loadEventsFromFirestore() {
    firestore.collection("events").get()
        .addOnSuccessListener { documents ->
            events.clear()
            for (document in documents) {
                val event = document.toObject(EventModel::class.java)
                event.id = document.id
                events.add(event)
            }
            eventAdapter.notifyDataSetChanged()
        }
        .addOnFailureListener {
            Toast.makeText(this, "Failed to load events", Toast.LENGTH_SHORT).show()
        }
}

Handling Image Uploads:

    private fun uploadImageToFirebase(imageUri: Uri, position: Int) {
    val storageRef = FirebaseStorage.getInstance().reference.child("event_images/${UUID.randomUUID()}.jpg")

    storageRef.putFile(imageUri)
        .addOnSuccessListener {
            storageRef.downloadUrl.addOnSuccessListener { uri ->
                events[position].imageUrl = uri.toString()
                eventAdapter.notifyItemChanged(position)
                Toast.makeText(this, "Image uploaded. Press 'Create' to save it.", Toast.LENGTH_SHORT).show()
            }
        }
        .addOnFailureListener {
            Toast.makeText(this, "Image upload failed", Toast.LENGTH_SHORT).show()
        }
    }


