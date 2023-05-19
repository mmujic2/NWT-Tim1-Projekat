import "./App.css";

import "../node_modules/bootstrap/dist/css/bootstrap.min.css";
import { Route,Routes } from "react-router-dom";
import RegisterPage from "./shared/RegisterPage/RegisterPage";
import PrivateRoute from "./shared/PrivateRoute/PrivateRoute"
import Sidebar from "./shared/util/Sidebar/Sidebar";
import Header from "./shared/util/Header";
import NotFound from "./shared/util/NotFound";


function App() {
  const options= new Map([['My information',"/customer/details"], ["Favourite restaurants","/restaurant/favorites"], ["Order history","/order-history"]])
  const icons= new Map([['My information',"user"], ["Favourite restaurants","heart"], ["Order history","receipt"]])
 
  return(
    <>
     
      <Routes>
        <Route path="/register" element={<RegisterPage></RegisterPage>} />
        <Route path="*" element={<NotFound></NotFound>}/>
        <Route path="/" element={<PrivateRoute />}>
          <Route path="/" element={<> <Sidebar optionsMap={options} iconsMap={icons}/><div style={{height:"1000px"}}></div></>}></Route>
        </Route>
        
      </Routes>
      </>
  )
}

export default App;
