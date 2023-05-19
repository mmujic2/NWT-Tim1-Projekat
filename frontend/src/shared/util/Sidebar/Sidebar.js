import React, { useState } from 'react';
import {
  CDBSidebar,
  CDBSidebarContent,
  CDBSidebarFooter,
  CDBSidebarHeader,
  CDBSidebarMenu,
  CDBSidebarMenuItem,
} from 'cdbreact';
import { NavLink,useLocation } from 'react-router-dom';
import styles from "./Sidebar.css"


const Sidebar = ({ optionsMap,iconsMap }) => {
  const location = useLocation();
  
  

  const options = () => {
    return (
      <>
        {[...optionsMap.keys()].map(k =>
        {
         

          return(
          <NavLink exact to={optionsMap.get(k)} style={{marginLeft:0}}>
            { location.pathname==optionsMap.get(k) ? <CDBSidebarMenuItem icon={iconsMap.get(k)} className='active' >{k}</CDBSidebarMenuItem> : 
            <CDBSidebarMenuItem icon={iconsMap.get(k)} className='option'>{k}</CDBSidebarMenuItem>}
            
          </NavLink>)
        }
        )}
      </>
    )
  }
  return (
    <div style={{position:"fixed",top:0,left:0,zIndex:100, display: 'flex', height: '100vh', overflow: 'scroll initial' }}>
      <CDBSidebar textColor="#333" backgroundColor="#D7D7D7" style={{paddingTop:"50px"}}>
        <CDBSidebarHeader prefix={<i className="fa fa-bars" />}></CDBSidebarHeader>
        <CDBSidebarContent className="sidebar-content">
          <CDBSidebarMenu>
            {options()}
          </CDBSidebarMenu>
        </CDBSidebarContent>
        
      </CDBSidebar>
    </div>
  );
};

export default Sidebar;
