package com.gahejodev.smartexpense

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter

class ExpenseViewModel(private val dao: ExpenseDao) : ViewModel() {
    val transactions = dao.observeTransactions().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val budgets = dao.observeBudgets().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    fun add(amount: Long, type: String, category: String, note: String, date: String) =
        viewModelScope.launch { dao.insertTransaction(ExpenseTransaction(amount = amount, type = type, category = category, note = note, date = date)) }
    fun delete(id: Long) = viewModelScope.launch { dao.deleteTransaction(id) }
    fun setBudget(month: String, amount: Long) = viewModelScope.launch { dao.saveBudget(MonthlyBudget(month, amount)) }
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val dao = ExpenseDatabase.get(applicationContext).dao()
        setContent {
            val vm: ExpenseViewModel = viewModel(factory = object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T = ExpenseViewModel(dao) as T
            })
            MaterialTheme { ExpenseScreen(vm) }
        }
    }
}

@Composable
fun ExpenseScreen(vm: ExpenseViewModel) {
    val transactions by vm.transactions.collectAsState()
    val budgets by vm.budgets.collectAsState()
    var month by remember { mutableStateOf(YearMonth.now().toString()) }
    var amount by remember { mutableStateOf("") }
    var type by remember { mutableStateOf("Expense") }
    var category by remember { mutableStateOf("Food") }
    var note by remember { mutableStateOf("") }
    var date by remember { mutableStateOf(LocalDate.now().toString()) }
    var budgetInput by remember { mutableStateOf("") }
    val filtered = transactions.filter { it.date.startsWith(month) }
    val income = filtered.filter { it.type == "Income" }.sumOf { it.amount }
    val spending = filtered.filter { it.type == "Expense" }.sumOf { it.amount }
    val budget = budgets.firstOrNull { it.month == month }?.amount ?: 0L
    val categories = if (type == "Expense") listOf("Food", "Transport", "Bills", "Shopping", "Health", "Other") else listOf("Salary", "Business", "Gift", "Other")
    LaunchedEffect(type) { if (category !in categories) category = categories.first() }
    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text("Smart Expense Tracker", style = MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(8.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedButton(onClick = { month = YearMonth.parse(month).minusMonths(1).toString() }) { Text("‹") }
            Text(month, modifier = Modifier.padding(top = 12.dp))
            OutlinedButton(onClick = { month = YearMonth.parse(month).plusMonths(1).toString() }) { Text("›") }
        }
        Text("Income: Rs $income    Expenses: Rs $spending")
        Text("Balance: Rs ${income - spending}", style = MaterialTheme.typography.titleMedium)
        Text("Budget: Rs $budget | Remaining: Rs ${budget - spending}")
        if (budget > 0) LinearProgressIndicator(progress = { (spending.toFloat() / budget).coerceIn(0f, 1f) }, modifier = Modifier.fillMaxWidth())
        HorizontalDivider(Modifier.padding(vertical = 8.dp))
        Column(Modifier.weight(1f).verticalScroll(rememberScrollState())) {
            Text("Add transaction", style = MaterialTheme.typography.titleMedium)
            OutlinedTextField(value = amount, onValueChange = { amount = it.filter(Char::isDigit) }, label = { Text("Amount (whole PKR)") }, modifier = Modifier.fillMaxWidth())
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf("Expense", "Income").forEach { item ->
                    FilterChip(selected = type == item, onClick = { type = item }, label = { Text(item) })
                }
            }
            Text("Category")
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp), modifier = Modifier.fillMaxWidth()) {
                var expanded by remember { mutableStateOf(false) }
                Box {
                    OutlinedButton(onClick = { expanded = true }) { Text(category) }
                    DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                        categories.forEach { item -> DropdownMenuItem(text = { Text(item) }, onClick = { category = item; expanded = false }) }
                    }
                }
            }
            OutlinedTextField(value = date, onValueChange = { date = it }, label = { Text("Date YYYY-MM-DD") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = note, onValueChange = { note = it }, label = { Text("Note (optional)") }, modifier = Modifier.fillMaxWidth())
            Button(onClick = {
                val value = amount.toLongOrNull()
                val parsed = runCatching { LocalDate.parse(date, DateTimeFormatter.ISO_LOCAL_DATE) }.getOrNull()
                if (value != null && value > 0 && parsed != null) {
                    vm.add(value, type, category, note.trim(), parsed.toString())
                    amount = ""; note = ""
                }
            }, enabled = (amount.toLongOrNull() ?: 0L) > 0 && runCatching { LocalDate.parse(date) }.isSuccess) { Text("Save Transaction") }
            HorizontalDivider(Modifier.padding(vertical = 8.dp))
            Text("Monthly budget", style = MaterialTheme.typography.titleMedium)
            OutlinedTextField(value = budgetInput, onValueChange = { budgetInput = it.filter(Char::isDigit) }, label = { Text("Budget in PKR") })
            Button(onClick = { budgetInput.toLongOrNull()?.let { vm.setBudget(month, it); budgetInput = "" } }, enabled = budgetInput.toLongOrNull() != null) { Text("Set Budget") }
            HorizontalDivider(Modifier.padding(vertical = 8.dp))
            Text("Transactions (${filtered.size})", style = MaterialTheme.typography.titleMedium)
            if (filtered.isEmpty()) Text("No transactions this month.")
            filtered.forEach { transaction ->
                Card(Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                    Row(Modifier.padding(12.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                        Column(Modifier.weight(1f)) {
                            Text("${transaction.category} • ${transaction.type}", style = MaterialTheme.typography.titleSmall)
                            Text("${transaction.date} • ${transaction.note}")
                            Text("Rs ${transaction.amount}")
                        }
                        TextButton(onClick = { vm.delete(transaction.id) }) { Text("Delete") }
                    }
                }
            }
        }
    }
}
