import { createApp } from 'vue'
import App from './App.vue'
import MainComponent from './components/MainComponent.vue'
import DoctoriComponent from './components/DoctoriComponent.vue'
import ReteteComponent from './components/ReteteComponent.vue'
import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  { path: '/', component: MainComponent },
  { path: '/doctori', component: DoctoriComponent },
  { path: '/retete', component: ReteteComponent }
]
const router = createRouter({
  history: createWebHistory(),
  routes
})

createApp(App).use(router).mount('#app')
