import "./App.css";

import "../node_modules/bootstrap/dist/css/bootstrap.min.css";
import { Route,Routes } from "react-router-dom";
import RegisterPage from "./shared/RegisterPage/RegisterPage";
import PrivateRoute from "./shared/PrivateRoute/PrivateRoute"
import Sidebar from "./shared/util/Sidebar/Sidebar";

function App() {
  const options= new Map([['My information',"/customer/details"], ["Favourite restaurants","restaurant/favoriters"], ["Order history","/order-history"]])
 
  return(
    <>
      
      <Routes>
        <Route path="/register" element={<RegisterPage></RegisterPage>} />
        <Route path="/" element={<PrivateRoute />}>
          <Route path="/" element={<>Home</>}></Route>
        </Route>
        
      </Routes>
      </>
  )
}

export default App;
