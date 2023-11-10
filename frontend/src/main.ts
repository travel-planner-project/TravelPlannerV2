import { createApp } from 'vue';
import 'bootstrap/dist/js/bootstrap.bundle.min';
import 'bootstrap-vue-3/dist/bootstrap-vue-3.css';
import { BootstrapVue3 } from 'bootstrap-vue-3';
import { VueQueryPlugin } from 'vue-query';
import { PiniaVuePlugin } from 'pinia';
import { library } from '@fortawesome/fontawesome-svg-core';
import { faUserSecret } from '@fortawesome/free-solid-svg-icons';
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome';
import App from './App.vue';

library.add(faUserSecret);

createApp(App)
  .use(VueQueryPlugin)
  .use(BootstrapVue3)
  .use(PiniaVuePlugin)
  .component('FontAwesomeIcon', FontAwesomeIcon)
  .mount('#app');
