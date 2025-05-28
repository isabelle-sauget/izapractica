<script setup>
import MainComponent from './components/MainComponent.vue'
import DoctoriComponent from './components/DoctoriComponent.vue'
import ReteteComponent from './components/ReteteComponent.vue'
import { ref } from 'vue'

const page = ref('pacienti')
</script>

<template>
  <div>
    <nav style="display: flex; gap: 1rem; justify-content: center; margin-bottom: 2rem;">
      <button @click="page = 'pacienti'" :class="{ active: page === 'pacienti' }">Pacienti</button>
      <button @click="page = 'doctori'" :class="{ active: page === 'doctori' }">Doctori</button>
      <button @click="page = 'retete'" :class="{ active: page === 'retete' }">Retete</button>
    </nav>
    <MainComponent v-if="page === 'pacienti'" />
    <DoctoriComponent v-else-if="page === 'doctori'" />
    <ReteteComponent v-else />
  </div>
</template>

<style scoped>
nav button {
  background: #38bdf8;
  color: #fff;
  border: none;
  border-radius: 6px;
  padding: 0.5rem 1.2rem;
  font-weight: bold;
  cursor: pointer;
  transition: background 0.2s;
}
nav button.active {
  background: #0ea5e9;
}
nav button:not(.active):hover {
  background: #fbbf24;
  color: #1e293b;
}
</style>
