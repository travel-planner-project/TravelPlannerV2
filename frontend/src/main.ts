import { createApp } from 'vue';
import 'bootstrap/dist/js/bootstrap.bundle.min';
import 'bootstrap-vue-3/dist/bootstrap-vue-3.css';
import { VueQueryPlugin } from 'vue-query';
import { PiniaVuePlugin } from 'pinia';
import App from './App.vue';

createApp(App).use(VueQueryPlugin).use(PiniaVuePlugin).mount('#app');
