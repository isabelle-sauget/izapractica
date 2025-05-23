<script setup>
import { ref, onMounted } from 'vue'

const form = ref({
  index1: '',
  field1: '',
  field2: '',
  field3: ''
})
const records = ref([])
const loading = ref(false)
const error = ref('')

const searchId = ref('')
const searchResult = ref(null)
const searchError = ref('')

const apiUrl = import.meta.env.VITE_API_URL || 'http://localhost:8080'

async function fetchRecords() {
  loading.value = true
  error.value = ''
  try {
    const res = await fetch(`${apiUrl}/api/records`)
    if (!res.ok) throw new Error('Failed to fetch records')
    records.value = await res.json()
  } catch (e) {
    error.value = e.message
  } finally {
    loading.value = false
  }
}

async function submitForm() {
  error.value = ''
  try {
    const res = await fetch(`${apiUrl}/api/records`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(form.value)
    })
    if (!res.ok) throw new Error('Failed to create record')
    form.value = { index1: '', field1: '', field2: '', field3: '' }
    await fetchRecords()
  } catch (e) {
    error.value = e.message
  }
}

const showDialog = ref(false)
const recordToDelete = ref(null)

function confirmDelete(recordId) {
  recordToDelete.value = recordId
  showDialog.value = true
}

async function deleteRecord() {
  if (!recordToDelete.value) return
  error.value = ''
  try {
    const res = await fetch(`${apiUrl}/api/records/${recordToDelete.value}`, {
      method: 'DELETE'
    })
    if (!res.ok) throw new Error('Failed to delete record')
    await fetchRecords()
  } catch (e) {
    error.value = e.message
  } finally {
    showDialog.value = false
    recordToDelete.value = null
  }
}
function cancelDelete() {
  showDialog.value = false
  recordToDelete.value = null
}

async function searchById() {
  searchError.value = ''
  searchResult.value = null
  if (!searchId.value) {
    searchError.value = 'Please enter an ID.'
    return
  }
  try {
    const res = await fetch(`${apiUrl}/api/records/${searchId.value}`)
    if (!res.ok) throw new Error('Record not found')
    searchResult.value = await res.json()
  } catch (e) {
    searchError.value = e.message
  }
}

onMounted(fetchRecords)
</script>

<template>
  <main class="main-content">
    <h1 class="gradient-title">Proiect Clinica</h1>
    <form @submit.prevent="submitForm" class="form">
      <label>Index1 <input v-model="form.index1" required /></label>
      <label>Field1 <input v-model="form.field1" required /></label>
      <label>Field2 <input v-model="form.field2" required /></label>
      <label>Field3 <input v-model="form.field3" required /></label>
      <button type="submit">Add Record</button>
    </form>
    <div v-if="error" class="error">{{ error }}</div>
    <section class="table-section">
      <div class="records-header">
        <h2>All Records</h2>
        <button class="refresh-btn" @click="fetchRecords">All Records</button>
      </div>
      <div v-if="loading">Loading...</div>
      <table v-if="records.length">
        <thead>
          <tr>
            <th>ID</th>
            <th>Index1</th>
            <th>Field1</th>
            <th>Field2</th>
            <th>Field3</th>
            <th>Delete</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="record in records" :key="record.id">
            <td>{{ record.id }}</td>
            <td>{{ record.index1 }}</td>
            <td>{{ record.field1 }}</td>
            <td>{{ record.field2 }}</td>
            <td>{{ record.field3 }}</td>
            <td><button @click="confirmDelete(record.id)">Delete</button></td>
          </tr>
        </tbody>
      </table>
      <div v-else>No records found.</div>
    </section>
    <!-- HTML Dialog for delete confirmation -->
    <dialog v-if="showDialog" open class="confirm-dialog">
      <div>Are you sure you want to delete this record?</div>
      <div class="dialog-actions">
        <button @click="deleteRecord">Yes, Delete</button>
        <button @click="cancelDelete">Cancel</button>
      </div>
    </dialog>
    <section class="search-section">
      <h2>Search by ID</h2>
      <form @submit.prevent="searchById">
        <input v-model="searchId" placeholder="Enter ID" />
        <button type="submit">Search</button>
      </form>
      <div v-if="searchError" class="error">{{ searchError }}</div>
      <div v-if="searchResult">
        <h3>Result:</h3>
        <div class="search-result">
          <div><strong>ID:</strong> {{ searchResult.id }}</div>
          <div><strong>Index1:</strong> {{ searchResult.index1 }}</div>
          <div><strong>Field1:</strong> {{ searchResult.field1 }}</div>
          <div><strong>Field2:</strong> {{ searchResult.field2 }}</div>
          <div><strong>Field3:</strong> {{ searchResult.field3 }}</div>
        </div>
      </div>
    </section>
  </main>
</template>

<style scoped>
.gradient-title {
  font-size: 2.5rem;
  font-weight: bold;
  background: linear-gradient(90deg, #1e293b, #38bdf8, #fbbf24);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
  color: #1e293b;
  margin-bottom: 2rem;
  text-align: center;
}
.form {
  display: flex;
  gap: 1rem;
  flex-wrap: wrap;
  margin-bottom: 1.5rem;
  justify-content: center;
}
.form label {
  display: flex;
  flex-direction: column;
  font-weight: 500;
  color: #1e293b;
}
.form input {
  padding: 0.4rem 0.7rem;
  border-radius: 6px;
  border: 1px solid #64748b;
  margin-top: 0.2rem;
  background: #f1f5f9;
  color: #1e293b;
}
.form button {
  background: linear-gradient(90deg, #38bdf8, #fbbf24);
  color: #1e293b;
  border: none;
  border-radius: 6px;
  padding: 0.5rem 1.2rem;
  font-weight: bold;
  cursor: pointer;
  transition: background 0.2s;
}
.form button:hover {
  background: linear-gradient(90deg, #0ea5e9, #f59e42);
  color: #fff;
}
.table-section {
  margin: 2rem 0;
}
.records-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 0.5rem;
}
.refresh-btn {
  background: linear-gradient(90deg, #38bdf8, #fbbf24);
  color: #1e293b;
  border: none;
  border-radius: 6px;
  padding: 0.4rem 1.1rem;
  font-weight: bold;
  cursor: pointer;
  font-size: 1rem;
  transition: background 0.2s, color 0.2s;
}
.refresh-btn:hover {
  background: linear-gradient(90deg, #0ea5e9, #f59e42);
  color: #fff;
}
table {
  width: 100%;
  border-collapse: collapse;
  margin: 0 auto;
  background: #f8fafc;
  box-shadow: 0 2px 8px rgba(30,41,59,0.07);
  color: #1e293b;
}
th, td {
  padding: 0.7rem 1rem;
  border-bottom: 1px solid #e2e8f0;
  text-align: left;
}
th {
  background: #e0e7ef;
  font-weight: 600;
}
td button {
  background: #fbbf24;
  color: #1e293b;
  border: none;
  border-radius: 4px;
  padding: 0.3rem 0.8rem;
  cursor: pointer;
  transition: background 0.2s;
}
td button:hover {
  background: #f59e42;
  color: #fff;
}
.error {
  color: #dc2626;
  margin: 1rem 0;
  text-align: center;
}
.search-section {
  margin: 2rem 0;
  text-align: center;
}
.search-section input {
  padding: 0.4rem 0.7rem;
  border-radius: 6px;
  border: 1px solid #64748b;
  margin-right: 0.5rem;
  background: #f1f5f9;
  color: #1e293b;
}
.search-section button {
  background: linear-gradient(90deg, #38bdf8, #fbbf24);
  color: #1e293b;
  border: none;
  border-radius: 6px;
  padding: 0.5rem 1.2rem;
  font-weight: bold;
  cursor: pointer;
  transition: background 0.2s;
}
.search-section button:hover {
  background: linear-gradient(90deg, #0ea5e9, #f59e42);
  color: #fff;
}
body, main {
  background: #f1f5f9;
  color: #1e293b;
}
.confirm-dialog {
  padding: 2rem 2.5rem;
  border-radius: 10px;
  border: none;
  box-shadow: 0 4px 24px rgba(30,41,59,0.18);
  background: #fff;
  color: #1e293b;
  font-size: 1.1rem;
  z-index: 1000;
  min-width: 320px;
  text-align: center;
}
.dialog-actions {
  margin-top: 1.5rem;
  display: flex;
  gap: 1.5rem;
  justify-content: center;
}
.confirm-dialog button {
  padding: 0.5rem 1.2rem;
  border-radius: 6px;
  border: none;
  font-weight: bold;
  cursor: pointer;
  background: #fbbf24;
  color: #1e293b;
  transition: background 0.2s, color 0.2s;
}
.confirm-dialog button:hover {
  background: #f59e42;
  color: #fff;
}
.search-result {
  margin: 1.5rem auto 0 auto;
  padding: 1.2rem 2rem;
  background: #f8fafc;
  border-radius: 10px;
  box-shadow: 0 2px 8px rgba(30,41,59,0.07);
  display: inline-block;
  text-align: left;
  font-size: 1.1rem;
  color: #1e293b;
}
.search-result div {
  margin-bottom: 0.5rem;
}
.search-result strong {
  color: #0ea5e9;
}
.main-content {
  background: #fff;
  border-radius: 16px;
  box-shadow: 0 4px 24px rgba(30,41,59,0.10);
  padding: 2.5rem 2rem;
  margin: 2.5rem auto;
  max-width: 900px;
}
</style>
