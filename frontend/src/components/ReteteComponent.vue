<script setup>
import { ref, onMounted } from 'vue'

const form = ref({
  doctorId: '',
  pacientId: '',
  data: '',
  reteta: '',
  compensata: 'DA'
})
const retete = ref([])
const doctori = ref([])
const pacienti = ref([])
const loading = ref(false)
const error = ref('')

const searchId = ref('')
const searchResult = ref(null)
const searchError = ref('')

const apiUrl = import.meta.env.VITE_API_URL || 'http://localhost:8080'

async function fetchRetete() {
  loading.value = true
  error.value = ''
  try {
    const res = await fetch(`${apiUrl}/api/retete`)
    if (!res.ok) throw new Error('Failed to fetch retete')
    retete.value = await res.json()
  } catch (e) {
    error.value = e.message
    console.log('fetchRetete error:', e)
  } finally {
    loading.value = false
  }
}
async function fetchDoctori() {
  try {
    const res = await fetch(`${apiUrl}/api/doctori`)
    if (!res.ok) throw new Error('Failed to fetch doctori')
    doctori.value = await res.json()
  } catch (e) {
    console.log('fetchDoctori error:', e)
  }
}
async function fetchPacienti() {
  try {
    const res = await fetch(`${apiUrl}/api/pacienti`)
    if (!res.ok) throw new Error('Failed to fetch pacienti')
    pacienti.value = await res.json()
  } catch (e) {
    console.log('fetchPacienti error:', e)
  }
}

async function submitForm() {
  error.value = ''
  try {
    const payload = {
      doctor: { id: form.value.doctorId },
      pacient: { id: form.value.pacientId },
      data: form.value.data,
      reteta: form.value.reteta,
      compensata: form.value.compensata
    }
    const res = await fetch(`${apiUrl}/api/retete`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(payload)
    })
    if (!res.ok) throw new Error('Failed to create reteta')
    form.value = { doctorId: '', pacientId: '', data: '', reteta: '', compensata: 'DA' }
    await fetchRetete()
  } catch (e) {
    error.value = e.message
    console.error('submitForm error:', e)
  }
}

const showDialog = ref(false)
const retetaToDelete = ref(null)

function confirmDelete(retetaId) {
  retetaToDelete.value = retetaId
  showDialog.value = true
}

async function deleteReteta() {
  if (!retetaToDelete.value) return
  error.value = ''
  try {
    const res = await fetch(`${apiUrl}/api/retete/${retetaToDelete.value}`, {
      method: 'DELETE'
    })
    if (!res.ok) throw new Error('Failed to delete reteta')
    await fetchRetete()
  } catch (e) {
    error.value = e.message
    console.error('deleteReteta error:', e)
  } finally {
    showDialog.value = false
    retetaToDelete.value = null
  }
}
function cancelDelete() {
  showDialog.value = false
  retetaToDelete.value = null
}

async function searchById() {
  searchError.value = ''
  searchResult.value = null
  if (!searchId.value) {
    searchError.value = 'Please enter an ID.'
    return
  }
  try {
    const res = await fetch(`${apiUrl}/api/retete/${searchId.value}`)
    if (!res.ok) throw new Error('Reteta not found')
    searchResult.value = await res.json()
  } catch (e) {
    searchError.value = e.message
    console.error('searchById error:', e)
  }
}

const editingId = ref(null)
const editForm = ref({
  doctorId: '',
  pacientId: '',
  data: '',
  reteta: '',
  compensata: 'DA'
})

function startEdit(reteta) {
  editingId.value = reteta.id
  editForm.value = {
    doctorId: reteta.doctor?.id || '',
    pacientId: reteta.pacient?.id || '',
    data: reteta.data,
    reteta: reteta.reteta,
    compensata: reteta.compensata
  }
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
    const payload = {
      doctor: { id: editForm.value.doctorId },
      pacient: { id: editForm.value.pacientId },
      data: editForm.value.data,
      reteta: editForm.value.reteta,
      compensata: editForm.value.compensata
    }
    const res = await fetch(`${apiUrl}/api/retete/${id}`, {
      method: 'PUT',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(payload)
    })
    if (!res.ok) throw new Error('Failed to update reteta')
    await fetchRetete()
    editingId.value = null
  } catch (e) {
    error.value = e.message
    console.error('saveEditConfirmed error:', e)
  } finally {
    showSaveDialog.value = false
  }
}

onMounted(() => {
  fetchRetete()
  fetchDoctori()
  fetchPacienti()
})
</script>

<template>
  <main class="main-content">
    <h1 class="gradient-title">Proiect Clinica - Retete</h1>
    <form @submit.prevent="submitForm" class="form">
      <label>Doctor
        <select v-model="form.doctorId" required>
          <option value="" disabled>Select Doctor</option>
          <option v-for="doc in doctori" :key="doc.id" :value="doc.id">
            {{ doc.nume }} {{ doc.prenume }}
          </option>
        </select>
      </label>
      <label>Pacient
        <select v-model="form.pacientId" required>
          <option value="" disabled>Select Pacient</option>
          <option v-for="pac in pacienti" :key="pac.id" :value="pac.id">
            {{ pac.nume }} {{ pac.prenume }}
          </option>
        </select>
      </label>
      <label>Data <input v-model="form.data" type="date" required /></label>
      <label>Reteta <textarea v-model="form.reteta" rows="4" style="resize:vertical; min-height: 80px;" required /></label>
      <label>Compensata
        <select v-model="form.compensata" required>
          <option value="DA">DA</option>
          <option value="NU">NU</option>
        </select>
      </label>
      <button type="submit">Adauga Reteta</button>
    </form>
    <div v-if="error" class="error">{{ error }}</div>
    <section class="table-section">
      <div class="records-header">
        <h2>Toate Retetele</h2>
        <button class="refresh-btn" @click="fetchRetete">Refresh</button>
      </div>
      <div v-if="loading">Loading...</div>
      <table v-if="retete.length || !loading">
        <thead>
          <tr>
            <th>ID</th>
            <th>Doctor</th>
            <th>Pacient</th>
            <th>Data</th>
            <th>Reteta</th>
            <th>Compensata</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          <tr v-if="!retete.length && !loading">
            <td colspan="7" style="text-align:center; color:#64748b;">No retete found.</td>
          </tr>
          <tr v-for="reteta in retete" :key="reteta.id">
            <td>{{ reteta.id }}</td>
            <td v-if="editingId !== reteta.id">{{ reteta.doctor?.nume }} {{ reteta.doctor?.prenume }}</td>
            <td v-else>
              <select v-model="editForm.doctorId">
                <option v-for="doc in doctori" :key="doc.id" :value="doc.id">
                  {{ doc.nume }} {{ doc.prenume }}
                </option>
              </select>
            </td>
            <td v-if="editingId !== reteta.id">{{ reteta.pacient?.nume }} {{ reteta.pacient?.prenume }}</td>
            <td v-else>
              <select v-model="editForm.pacientId">
                <option v-for="pac in pacienti" :key="pac.id" :value="pac.id">
                  {{ pac.nume }} {{ pac.prenume }}
                </option>
              </select>
            </td>
            <td v-if="editingId !== reteta.id">{{ reteta.data }}</td>
            <td v-else><input v-model="editForm.data" type="date" /></td>
            <td v-if="editingId !== reteta.id">{{ reteta.reteta }}</td>
            <td v-else><textarea v-model="editForm.reteta" rows="4" style="resize:vertical; min-height: 80px;" /></td>
            <td v-if="editingId !== reteta.id">{{ reteta.compensata }}</td>
            <td v-else>
              <select v-model="editForm.compensata">
                <option value="DA">DA</option>
                <option value="NU">NU</option>
              </select>
            </td>
            <td>
              <template v-if="editingId === reteta.id">
                <button class="save-btn" @click="confirmSaveEdit">Save</button>
                <button class="cancel-btn" @click="cancelEdit">Cancel</button>
              </template>
              <template v-else>
                <button class="edit-btn" @click="startEdit(reteta)">Edit</button>
                <button @click="confirmDelete(reteta.id)">Delete</button>
              </template>
            </td>
          </tr>
        </tbody>
      </table>
    </section>
    <dialog v-if="showDialog" open class="confirm-dialog">
      <div>Are you sure you want to delete this reteta?</div>
      <div class="dialog-actions">
        <button @click="deleteReteta">Yes, Delete</button>
        <button @click="cancelDelete">Cancel</button>
      </div>
    </dialog>
    <dialog v-if="showSaveDialog" open class="confirm-dialog">
      <div>Are you sure you want to save the changes to this reteta?</div>
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
          <div><strong>Doctor:</strong> {{ searchResult.doctor?.nume }} {{ searchResult.doctor?.prenume }}</div>
          <div><strong>Pacient:</strong> {{ searchResult.pacient?.nume }} {{ searchResult.pacient?.prenume }}</div>
          <div><strong>Data:</strong> {{ searchResult.data }}</div>
          <div><strong>Reteta:</strong> {{ searchResult.reteta }}</div>
          <div><strong>Compensata:</strong> {{ searchResult.compensata }}</div>
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
.form input, .form select, .form textarea {
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
.table-section input, .table-section select {
  padding: 0.3rem 0.6rem;
  border-radius: 4px;
  border: 1px solid #bdbdbd;
  width: 100%;
  box-sizing: border-box;
}
</style>
