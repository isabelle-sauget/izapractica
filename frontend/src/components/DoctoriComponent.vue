<script setup>
import { ref, onMounted } from 'vue'

const form = ref({
  nume: '',
  prenume: '',
  cnp: '',
  adresa: '',
  telefon: '',
  email: '',
  specializare: '',
  clinica: '',
  alteInformatii: ''
})
const doctori = ref([])
const loading = ref(false)
const error = ref('')

const searchId = ref('')
const searchResult = ref(null)
const searchError = ref('')

const apiUrl = import.meta.env.VITE_API_URL || 'http://localhost:8080'

async function fetchDoctori() {
  loading.value = true
  error.value = ''
  try {
    const res = await fetch(`${apiUrl}/api/doctori`)
    if (!res.ok) throw new Error('Failed to fetch doctori')
    doctori.value = await res.json()
  } catch (e) {
    error.value = e.message
  } finally {
    loading.value = false
  }
}

async function submitForm() {
  error.value = ''
  try {
    const res = await fetch(`${apiUrl}/api/doctori`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(form.value)
    })
    if (!res.ok) throw new Error('Failed to create doctor')
    form.value = { nume: '', prenume: '', cnp: '', adresa: '', telefon: '', email: '', specializare: '', clinica: '', alteInformatii: '' }
    await fetchDoctori()
  } catch (e) {
    error.value = e.message
  }
}

const showDialog = ref(false)
const doctorToDelete = ref(null)

function confirmDelete(doctorId) {
  doctorToDelete.value = doctorId
  showDialog.value = true
}

async function deleteDoctor() {
  if (!doctorToDelete.value) return
  error.value = ''
  try {
    const res = await fetch(`${apiUrl}/api/doctori/${doctorToDelete.value}`, {
      method: 'DELETE'
    })
    if (!res.ok) throw new Error('Failed to delete doctor')
    await fetchDoctori()
  } catch (e) {
    error.value = e.message
  } finally {
    showDialog.value = false
    doctorToDelete.value = null
  }
}
function cancelDelete() {
  showDialog.value = false
  doctorToDelete.value = null
}

async function searchById() {
  searchError.value = ''
  searchResult.value = null
  if (!searchId.value) {
    searchError.value = 'Please enter an ID.'
    return
  }
  try {
    const res = await fetch(`${apiUrl}/api/doctori/${searchId.value}`)
    if (!res.ok) throw new Error('Doctor not found')
    searchResult.value = await res.json()
  } catch (e) {
    searchError.value = e.message
  }
}

const editingId = ref(null)
const editForm = ref({
  nume: '',
  prenume: '',
  cnp: '',
  adresa: '',
  telefon: '',
  email: '',
  specializare: '',
  clinica: '',
  alteInformatii: ''
})

function startEdit(doctor) {
  editingId.value = doctor.id
  editForm.value = { ...doctor }
}
function cancelEdit() {
  editingId.value = null
}
const showSaveDialog = ref(false)

function confirmSaveEdit() {
  showSaveDialog.value = true
}
function cancelSaveDialog() {
  showSaveDialog.value = false
}
async function saveEditConfirmed(id) {
  error.value = ''
  try {
    const res = await fetch(`${apiUrl}/api/doctori/${id}`, {
      method: 'PUT',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(editForm.value)
    })
    if (!res.ok) throw new Error('Failed to update doctor')
    await fetchDoctori()
    editingId.value = null
  } catch (e) {
    error.value = e.message
  } finally {
    showSaveDialog.value = false
  }
}

onMounted(fetchDoctori)
</script>

<template>
  <main class="main-content">
    <h1 class="gradient-title">Proiect Clinica - Doctori</h1>
    <form @submit.prevent="submitForm" class="form">
      <label>Nume <input v-model="form.nume" required /></label>
      <label>Prenume <input v-model="form.prenume" required /></label>
      <label>CNP <input v-model="form.cnp" required /></label>
      <label>Telefon <input v-model="form.telefon" required /></label>
      <label>Adresa <input v-model="form.adresa" required /></label>
      <label>Email <input v-model="form.email" type="email" required /></label>
      <label>Specializare <input v-model="form.specializare" required /></label>
      <label>Clinica <input v-model="form.clinica" required /></label>
      <label>Alte Informatii <input v-model="form.alteInformatii" required /></label>
      <button type="submit">Adauga Doctor</button>
    </form>
    <div v-if="error" class="error">{{ error }}</div>
    <section class="table-section">
      <div class="records-header">
        <h2>Toti Doctorii</h2>
        <button class="refresh-btn" @click="fetchDoctori">Refresh</button>
      </div>
      <div v-if="loading">Loading...</div>
      <table v-if="doctori.length">
        <thead>
          <tr>
            <th>ID</th>
            <th>Nume</th>
            <th>Prenume</th>
            <th>CNP</th>
            <th>Telefon</th>
            <th>Adresa</th>
            <th>Email</th>
            <th>Specializare</th>
            <th>Clinica</th>
            <th>Alte Informatii</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="doctor in doctori" :key="doctor.id">
            <td>{{ doctor.id }}</td>
            <td v-if="editingId !== doctor.id">{{ doctor.nume }}</td>
            <td v-else><input v-model="editForm.nume" /></td>
            <td v-if="editingId !== doctor.id">{{ doctor.prenume }}</td>
            <td v-else><input v-model="editForm.prenume" /></td>
            <td v-if="editingId !== doctor.id">{{ doctor.cnp }}</td>
            <td v-else><input v-model="editForm.cnp" /></td>
            <td v-if="editingId !== doctor.id">{{ doctor.telefon }}</td>
            <td v-else><input v-model="editForm.telefon" /></td>
            <td v-if="editingId !== doctor.id">{{ doctor.adresa }}</td>
            <td v-else><input v-model="editForm.adresa" /></td>
            <td v-if="editingId !== doctor.id">{{ doctor.email }}</td>
            <td v-else><input v-model="editForm.email" type="email" /></td>
            <td v-if="editingId !== doctor.id">{{ doctor.specializare }}</td>
            <td v-else><input v-model="editForm.specializare" /></td>
            <td v-if="editingId !== doctor.id">{{ doctor.clinica }}</td>
            <td v-else><input v-model="editForm.clinica" /></td>
            <td v-if="editingId !== doctor.id">{{ doctor.alteInformatii }}</td>
            <td v-else><input v-model="editForm.alteInformatii" /></td>
            <td>
              <template v-if="editingId === doctor.id">
                <button class="save-btn" @click="confirmSaveEdit">Save</button>
                <button class="cancel-btn" @click="cancelEdit">Cancel</button>
              </template>
              <template v-else>
                <button class="edit-btn" @click="startEdit(doctor)">Edit</button>
                <button @click="confirmDelete(doctor.id)">Delete</button>
              </template>
            </td>
          </tr>
        </tbody>
      </table>
      <div v-else>No doctori found.</div>
    </section>
    <dialog v-if="showDialog" open class="confirm-dialog">
      <div>Are you sure you want to delete this doctor?</div>
      <div class="dialog-actions">
        <button @click="deleteDoctor">Yes, Delete</button>
        <button @click="cancelDelete">Cancel</button>
      </div>
    </dialog>
    <dialog v-if="showSaveDialog" open class="confirm-dialog">
      <div>Are you sure you want to save the changes to this doctor?</div>
      <div class="dialog-actions">
        <button @click="saveEditConfirmed(editingId)">Yes, Save</button>
        <button @click="cancelSaveDialog">Cancel</button>
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
          <div><strong>Nume:</strong> {{ searchResult.nume }}</div>
          <div><strong>Prenume:</strong> {{ searchResult.prenume }}</div>
          <div><strong>CNP:</strong> {{ searchResult.cnp }}</div>
          <div><strong>Telefon:</strong> {{ searchResult.telefon }}</div>
          <div><strong>Adresa:</strong> {{ searchResult.adresa }}</div>
          <div><strong>Email:</strong> {{ searchResult.email }}</div>
          <div><strong>Specializare:</strong> {{ searchResult.specializare }}</div>
          <div><strong>Clinica:</strong> {{ searchResult.clinica }}</div>
          <div><strong>Alte Informatii:</strong> {{ searchResult.alteInformatii }}</div>
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
  max-width: 1200px;
}
.edit-btn, .save-btn, .cancel-btn {
  margin-right: 0.3rem;
  background: #38bdf8;
  color: #fff;
  border: none;
  border-radius: 4px;
  padding: 0.3rem 0.8rem;
  font-weight: bold;
  cursor: pointer;
  transition: background 0.2s;
}
.save-btn {
  background: #22c55e;
}
.cancel-btn {
  background: #fbbf24;
  color: #1e293b;
}
.edit-btn:hover {
  background: #0ea5e9;
}
.save-btn:hover {
  background: #16a34a;
}
.cancel-btn:hover {
  background: #f59e42;
  color: #fff;
}
.table-section input {
  padding: 0.3rem 0.6rem;
  border-radius: 4px;
  border: 1px solid #bdbdbd;
  width: 100%;
  box-sizing: border-box;
}
</style>
