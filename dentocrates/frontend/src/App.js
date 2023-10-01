import React from "react";
import { BrowserRouter as Router, Route, Routes } from "react-router-dom";
import LoginPage from "./components/client/authentication/login/LoginPage";
import RegisterPage from "./components/client/authentication/register/RegisterPage";
import Home from "./components/home/Home";
import NavigationBar from "./components/navigationBar/NavigationBar";
import {ClientPage} from "./components/client/ClientPage";
import {DentistPage} from "./components/dentist/DentistPage";
import {ClinicSelectorPage} from "./components/clinic/clinic/search/ClinicSelectorPage";
import { ClinicPage } from "./components/clinic/clinic/ClinicPage";
import LocationRegisterPage from "./components/clinic/location/LocationRegisterPage";
import ClinicRegisterPage from "./components/clinic/clinic/registration/ClinicRegisterPage.jsx";
import Calendar from "./components/clinic/calendar/Calendar";
import {Appointment} from "./components/appointment/Appointment";
import VerifyPage from "./components/client/authentication/verification/Verification";
import OauthLoginPage from "./components/client/authentication/login/OauthRedirectPage";
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
          <Route path="/login/oauth2/code" element={<OauthLoginPage />} />
        </Routes>
      </Router>
  );
}
export default App;
