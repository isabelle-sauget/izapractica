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

async function deleteRecord(id) {
  error.value = ''
  try {
    const res = await fetch(`${apiUrl}/api/records/${id}`, {
      method: 'DELETE'
    })
    if (!res.ok) throw new Error('Failed to delete record')
    await fetchRecords()
  } catch (e) {
    error.value = e.message
  }
}

async function searchById() {
  searchError.value = ''
  searchResult.value = null
  if (!searchId.value) return
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
  <div class="page-title">Proiect Clinica</div>
  <div class="form-card">
    <h2>Add New Record</h2>
    <form @submit.prevent="submitForm">
      <div class="form-group">
        <label for="index1">Index1</label>
        <input id="index1" v-model="form.index1" required />
      </div>
      <div class="form-group">
        <label for="field1">Field1</label>
        <input id="field1" v-model="form.field1" required />
      </div>
      <div class="form-group">
        <label for="field2">Field2</label>
        <input id="field2" v-model="form.field2" required />
      </div>
      <div class="form-group">
        <label for="field3">Field3</label>
        <input id="field3" v-model="form.field3" required />
      </div>
      <button type="submit">Add Record</button>
    </form>
    <div v-if="error" class="error">{{ error }}</div>
  </div>

  <div class="search-card">
    <h2>Search Record by ID</h2>
    <form @submit.prevent="searchById">
      <div class="form-group">
        <label for="searchId">Record ID</label>
        <input id="searchId" v-model="searchId" type="number" min="1" required />
        <button type="submit">Search</button>
      </div>
    </form>
    <div v-if="searchError" class="error">{{ searchError }}</div>
    <div v-if="searchResult" class="search-result visible">
      <strong>Result:</strong>
      <div><span class="label">ID:</span> <span class="value">{{ searchResult.id }}</span></div>
      <div><span class="label">Index1:</span> <span class="value">{{ searchResult.index1 }}</span></div>
      <div><span class="label">Field1:</span> <span class="value">{{ searchResult.field1 }}</span></div>
      <div><span class="label">Field2:</span> <span class="value">{{ searchResult.field2 }}</span></div>
      <div><span class="label">Field3:</span> <span class="value">{{ searchResult.field3 }}</span></div>
    </div>
  </div>

  <div class="records-list">
    <h2>All Records</h2>
    <div v-if="loading">Loading...</div>
    <table v-else :class="{ visible: records.length > 0 }">
      <thead>
        <tr>
          <th>ID</th>
          <th>Index1</th>
          <th>Field1</th>
          <th>Field2</th>
          <th>Field3</th>
          <th>Actions</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="rec in records" :key="rec.id">
          <td>{{ rec.id }}</td>
          <td>{{ rec.index1 }}</td>
          <td>{{ rec.field1 }}</td>
          <td>{{ rec.field2 }}</td>
          <td>{{ rec.field3 }}</td>
          <td>
            <button class="delete-btn" @click="deleteRecord(rec.id)">Delete</button>
          </td>
        </tr>
      </tbody>
    </table>
    <div v-if="!loading && records.length === 0" class="no-records">No records found.</div>
  </div>
</template>

<style scoped>
.page-title {
  text-align: center;
  font-size: 2.2em;
  font-weight: 800;
  margin: 1.5em 0 1.5em 0;
  color: #1e293b;
  background: linear-gradient(90deg, #6366f1 0%, #38bdf8 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}
.form-card {
  background: #f0f4ff;
  padding: 2em;
  border-radius: 12px;
  box-shadow: 0 2px 12px rgba(60, 80, 180, 0.10);
  margin-bottom: 2em;
  max-width: 400px;
  margin-left: auto;
  margin-right: auto;
}
.form-card h2,
.search-card h2 {
  color: #2563eb;
  background: linear-gradient(90deg, #38bdf8 0%, #6366f1 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
  font-size: 1.5em;
  font-weight: 700;
  margin-bottom: 1em;
  text-align: center;
}
.form-group {
  margin-bottom: 1em;
  text-align: left;
}
label {
  display: block;
  margin-bottom: 0.3em;
  font-weight: 600;
  color: #2a3a5a;
}
input {
  width: 100%;
  padding: 0.5em;
  border: 1px solid #b0b8d1;
  border-radius: 6px;
  background: #fff;
  color: #2a3a5a;
}
button {
  margin-top: 1em;
  width: 100%;
  background: #3b82f6;
  color: #fff;
  border: none;
  padding: 0.7em;
  border-radius: 6px;
  font-size: 1em;
  font-weight: 600;
  cursor: pointer;
  transition: background 0.2s;
}
button:hover {
  background: #2563eb;
}
.error {
  color: #d32f2f;
  margin-top: 1em;
}
.search-card {
  background: #f0f4ff;
  padding: 2em;
  border-radius: 12px;
  box-shadow: 0 2px 12px rgba(60, 80, 180, 0.10);
  margin-bottom: 2em;
  max-width: 400px;
  margin-left: auto;
  margin-right: auto;
}
.search-card h2 {
  color: #2563eb;
  background: linear-gradient(90deg, #38bdf8 0%, #6366f1 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
  font-size: 1.5em;
  font-weight: 700;
  margin-bottom: 1em;
  text-align: center;
}
.search-result {
  background: #e0f7fa;
  border-radius: 10px;
  padding: 1.2em;
  margin-top: 1em;
  box-shadow: 0 2px 8px rgba(0, 184, 212, 0.10);
  color: #006064;
  font-size: 1.08em;
  display: none;
}
.search-result.visible {
  display: block;
}
.search-result .label {
  font-weight: 700;
  color: #00838f;
  margin-right: 0.5em;
}
.search-result .value {
  color: #004d40;
}
.records-list {
  background: #f8fafc;
  padding: 2em;
  border-radius: 12px;
  box-shadow: 0 2px 12px rgba(60, 80, 180, 0.08);
  max-width: 900px;
  margin-left: auto;
  margin-right: auto;
}
table {
  width: 100%;
  border-collapse: collapse;
  margin-top: 1em;
  background: #fff;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 1px 4px rgba(60, 80, 180, 0.06);
  display: none;
}
table.visible {
  display: table;
}
th, td {
  border: 1px solid #e0e7ef;
  padding: 0.7em 1em;
  text-align: left;
  color: #2a3a5a;
}
th {
  background: #e0e7ef;
  font-weight: 700;
}
.no-records {
  color: #888;
  margin-top: 1em;
  text-align: center;
}
.delete-btn {
  background: #ef4444;
  color: #fff;
  border: none;
  padding: 0.4em 1em;
  border-radius: 4px;
  font-size: 0.95em;
  font-weight: 500;
  cursor: pointer;
  margin-left: 0.5em;
  transition: background 0.2s;
}
.delete-btn:hover {
  background: #b91c1c;
}
</style>
