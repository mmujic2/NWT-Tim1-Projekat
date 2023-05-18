import React from 'react';
import {
  CDBSidebar,
  CDBSidebarContent,
  CDBSidebarMenu,
  CDBSidebarMenuItem,
} from 'cdbreact';
import { NavLink } from 'react-router-dom';
import styles from "./Sidebar.css"

const Sidebar = ({optionsMap}) => {

    const options=()=> {
        return(
          <>
        {[...optionsMap.keys()].map(k=> 
            <NavLink exact to={optionsMap.get(k)} activeClassName="activeClicked">
              <CDBSidebarMenuItem className={styles.activeClicked}>{k}</CDBSidebarMenuItem>
            </NavLink>
        )}
        </>
        )
    }
  return (
    <div style={{ display: 'flex', height: '100vh', overflow: 'scroll initial' }}>
      <CDBSidebar textColor="#333" backgroundColor="#D7D7D7">
       
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
