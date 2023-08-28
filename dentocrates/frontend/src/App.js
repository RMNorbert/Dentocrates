import React from "react";
import { BrowserRouter as Router, Route, Routes } from "react-router-dom";
import LoginPage from "./components/client/LoginPage";
import RegisterPage from "./components/client/RegisterPage";
import Home from "./components/Home";
import NavigationBar from "./components/NavigationBar";
import {ClientPage} from "./components/client/ClientPage";
import {DentistPage} from "./components/client/DentistPage";
import {ClinicSelectorPage} from "./components/clinic/ClinicSelectorPage";
import { ClinicPage } from "./components/clinic/ClinicPage";
import LocationRegisterPage from "./components/clinic/location/LocationRegisterPage";
import ClinicRegisterPage from "./components/clinic/ClinicRegisterPage.jsx";
import Calendar from "./components/clinic/calendar/Calendar";
import {Appointment} from "./components/client/Appointment";
import VerifyPage from "./components/client/Verification";
import AuthLoginPage from "./components/client/AuthLogin";
function App() {
  return (
      <Router>
        <NavigationBar />
        <Routes>
          <Route path="/" element={<LoginPage />} />
          <Route path="/home" element={<Home />} />
          <Route path="/register" element={<RegisterPage />} />
          <Route path="/client" element={<ClientPage/>} />
          <Route path="/appointments" element={<Appointment/>} />
          <Route path="/dentist" element={<DentistPage/>} />
          <Route path="/clinic" element={<ClinicSelectorPage />} />
          <Route path="/clinic/:id" element={<ClinicPage />} />
          <Route path="/calendar/:id" element={<Calendar />} />
          <Route path="/location" element={<LocationRegisterPage />} />
          <Route path="/clinic/register" element={<ClinicRegisterPage />} />
          <Route path="/verify/:verificationCode" element={<VerifyPage reset={false} />} />
          <Route path="/verify/reset/:verificationCode" element={<VerifyPage reset={true} />} />
          <Route path="/login/oauth2/code" element={<AuthLoginPage />} />
        </Routes>
      </Router>
  );
}
export default App;
