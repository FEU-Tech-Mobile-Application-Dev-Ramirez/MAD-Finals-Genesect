Welcome Screen and Navigation Logic Documentation

Welcome Screen

-The welcome screen, represented by AdminHomeActivity, serves as the entry point for administrators. It includes functionalities such as:
-Displaying the admin username fetched from Firestore.
-Navigating to the MenuActivity.

Code Breakdown

Initializing Components:

    private lateinit var binding: AdminHomeBinding
    private lateinit var adminAdapter: AdminAdapter
    private val users = mutableListOf<UserModel>()
    private val firestore = FirebaseFirestore.getInstance()

onCreate Method:
    override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = AdminHomeBinding.inflate(layoutInflater)
    setContentView(binding.root)

    setupRecyclerView()
    loadUsers()

    binding.btnAdminSignUp.setOnClickListener {
        startActivity(Intent(this, AdminSignupActivity::class.java))

-Updates UI elements such as the username.
-Handles button clicks to navigate to the MenuActivity and AdminSignupActivity.

Handling User Navigation
    binding.btnMenu.setOnClickListener {
    val intent = Intent(this, MenuActivity::class.java)
    intent.putExtra("username", currentUsername)
    intent.putExtra("role", currentUserRole)
    intent.putExtra("email", currentUserEmail)
    startActivity(intent)
    }



Admin Signup Page

The signup page, represented by AdminSignupActivity, enables administrators to create an account. It includes:

    -Form validation for username, email, and password.
    -Password complexity validation.
    -Firebase Authentication for user registration.
    -Storing user details in Firestore.


Initializing Components:
    private lateinit var binding: AdminSignupBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore


Handling Sign-Up Process:
    binding.btnCreateAdmin.setOnClickListener {
    val username = binding.etAdminUsername.text.toString().trim()
    val email = binding.etAdminEmail.text.toString().trim()
    val password = binding.etAdminPassword.text.toString().trim()
    val confirmPassword = binding.etConfirmPassword.text.toString().trim()

    if (username.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
        Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
        return@setOnClickListener
    }

    if (!isValidPassword(password)) {
        Toast.makeText(
            this,
            "Invalid password: Must be 12+ characters, include 1 upper, 1 lower, 1 special, and no invalid symbols",
            Toast.LENGTH_LONG
        ).show()
        return@setOnClickListener
    }

    if (password != confirmPassword) {
        Toast.makeText(this, "Passwords do not match!", Toast.LENGTH_SHORT).show()
        return@setOnClickListener
    }

    createAdminAccount(username, email, password)
    
    }

-Validates form inputs.
-Ensures password meets complexity requirements.
-Calls createAdminAccount method for registration.

Creating Admin Account:
    private fun createAdminAccount(username: String, email: String, password: String) {
    auth.createUserWithEmailAndPassword(email, password)
        .addOnSuccessListener { authResult ->
            val adminId = authResult.user?.uid ?: return@addOnSuccessListener
            val adminData = hashMapOf(
                "username" to username,
                "email" to email,
                "role" to "Admin"
            )

            firestore.collection("users").document(adminId)
                .set(adminData)
                .addOnSuccessListener {
                    Toast.makeText(this, "Admin created successfully!", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, LoginActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    finish()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Failed to save admin data", Toast.LENGTH_SHORT).show()
                }
        }
                    }

-Registers the user with Firebase Authentication.
-Stores the admin’s data in Firestore.
-Redirects the admin to the LoginActivity after successful signup.


Password Validation:
    private fun isValidPassword(password: String): Boolean {
        val passwordRegex = """^(?=.*[A-Z])(?=.*[a-z])(?=.*\d)(?=.*[@#$%&!])[A-Za-z\d@#$%&!]{12,}$""".toRegex()
        val forbiddenChars = listOf('^', '\'', '<', '>', '/')
        return password.matches(passwordRegex) && forbiddenChars.none { password.contains(it) }
    }
-Ensures passwords meet security criteria.
-Rejects invalid characters.


Login Page
The login page, LoginActivity, manages user authentication and role-based navigation.

User Login:
    binding.loginButton.setOnClickListener {
    val email = binding.emailInput.text.toString().trim()
    val password = binding.passwordInput.text.toString().trim()
    val isUserChecked = binding.userCheckbox.isChecked
    val isOrganizerChecked = binding.organizerCheckbox.isChecked

    if (email.isEmpty() || password.isEmpty()) {
        showToast("Please enter both email and password")
        return@setOnClickListener
    }

    auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
        if (!task.isSuccessful) {
            showToast("Login failed. Check your credentials")
        } else {
            val userId = auth.currentUser?.uid ?: return@addOnCompleteListener
            db.collection("users").document(userId).get().addOnCompleteListener { docTask ->
                if (!docTask.isSuccessful || docTask.result == null || !docTask.result.exists()) {
                    showToast("User not found in database")
                } else {
                    val role = docTask.result.getString("role") ?: ""
                    val targetActivity = when {
                        role == "Admin" -> AdminHomeActivity::class.java
                        isUserChecked && role == "User" -> UserHomeActivity::class.java
                        isOrganizerChecked && role == "Organizer" -> OrganizerHomeActivity::class.java
                        else -> null
                        }
                    targetActivity?.let { navigateTo(it) } ?: showToast("Role mismatch. Please select the correct role")
                    }
                }
            }
        }
    }

