package yestoya.c14220331.c14220331_todolist

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tugastodolist.Todo
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var todoAdapter: TodoAdapter
    private val todoList = mutableListOf<Todo>()

    private val addTodoLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val todoText = result.data?.getStringExtra("TODO_TEXT") ?: ""
            val deadlineMillis = result.data?.getLongExtra("TODO_DEADLINE", 0)
            val deadline = if (deadlineMillis!! > 0) Date(deadlineMillis) else null

            if (todoText.isNotBlank()) {
                val newTodo = Todo(todoText, deadline = deadline)
                todoList.add(newTodo)
                todoAdapter.updateList(todoList)
            }
        }
    }

    val editTodoLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val todoId = result.data?.getIntExtra("TODO_ID", -1) ?: -1
            if (todoId != -1 && todoId < todoList.size) {
                val todoText = result.data?.getStringExtra("TODO_TEXT") ?: ""
                val deadlineMillis = result.data?.getLongExtra("TODO_DEADLINE", 0)
                val deadline = if (deadlineMillis!! > 0) Date(deadlineMillis) else null
                todoList[todoId] = Todo(todoText, deadline = deadline)
                todoAdapter.updateList(todoList)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.rvTODO)
        recyclerView.layoutManager = LinearLayoutManager(this)
        todoAdapter = TodoAdapter(todoList)
        recyclerView.adapter = todoAdapter

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        findViewById<View>(R.id.addItemButton).setOnClickListener {
            val intent = Intent(this, AddTodoActivity::class.java)
            addTodoLauncher.launch(intent)
        }
    }
}