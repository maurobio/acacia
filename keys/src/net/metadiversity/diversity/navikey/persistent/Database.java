/*
 * Copyright (C) 2001-2006 University Bayreuth, BayCEER, Dept. of Mycology 
 * 
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License 
 * along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
package net.metadiversity.diversity.navikey.persistent;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;

/**
 * Copyright (C) 1998 Free Software Foundation, Inc.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2, or (at your option)
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, you can either send email to this
 * program's author (see below) or write to: The Free Software Foundation,
 * Inc.; 675 Mass Ave. Cambridge, MA 02139, USA.
 *
 *
 *
 * This class encapsulates database connections in JDBC.
 */

public class Database
{
    Connection con;
    ResultSet rs;
    Statement stmt;
    String jdbcStack;
    String url;
    String userName;
    String password;
    
    
    /**
     * Connect to the database
     * Examples jdbc stacks:
     * COM.imaginary.sql.msql.MsqlDriver
     * connect.microsoft.MicrosoftDriver
     * sun.jdbc.odbc.JdbcOdbcDriver
     *
     * Example URLs
     *
     * jdbc:msql://pnp.huh.harvard.edu:1114/index
     * jdbc:microsoft://django.harvard.edu:1433/index
     * jdbc:odbc:names
     */
    
    public void connect(String jdbcStack, String url, String userName, String password)
    throws Exception
    {
        this.jdbcStack = jdbcStack;
        this.url = url;
        this.userName = userName;
        this.password = password;
        
        try
        {
            
            // find the jdbc stack
            Class.forName(jdbcStack);
            
            System.out.println("Connecting to " + url);
            
            // get the connection to the database
            con = DriverManager.getConnection(url, userName, password);
        }
        catch(Exception e)
        {
            System.err.println("System Exeception in connect");
            System.err.println(e);
            throw e;
        }
    }
    
    public void reconnect()
    {
        try
        {
            connect(jdbcStack, url, userName, password);
        }
        catch(Exception e)
        {
            System.err.println("System Exeception in reconnect");
            System.err.println(e);
        }
    }
    
    public void closeConnection()
    throws Exception
    {
        try
        {
            System.out.println("Closing connection");
            con.close();
            rs.close();
            stmt.close();
        }
        catch (Exception e)
        {
            System.err.println("System Exception in closeConnection");
            System.err.println(e);
            throw e;
        }
    }
    
    private void checkForWarning(SQLWarning warn)
    throws SQLException
    {
        boolean rc = false;
        
        if (warn != null)
        {
            System.out.println("*Warning*");
            rc = true;
            while (warn != null)
            {
                System.out.println("SQLState: " + warn.getSQLState());
                System.out.println("Message:  " + warn.getMessage());
                System.out.println("Vendor:   " + warn.getErrorCode());
                warn = warn.getNextWarning();
            }
        }
    }
    
    /**
     * For queries
     */
    public ResultSet select(String query)
    throws Exception
    {
        try
        {
            if(con.isClosed())
            {
                System.err.println("Select: connection is not open");
                reconnect();
            }
            stmt = con.createStatement();
            rs = stmt.executeQuery(query);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return rs;
    }
    
    /**
     * For updates, inserts, and deletes
     */
    public void update(String query)
    throws Exception
    {
        try
        {
            if(con.isClosed())
            {
                System.err.println("Update: connection is not open");
                reconnect();
            }
            stmt = con.createStatement();
            stmt.executeUpdate(query);
        }
        catch (Exception e)
        {
            System.err.println("Update failed:" + e);
      /*
      if(con != null){
        try{
          con.rollback();
        }
        catch(SQLException ex){
          ex.printStackTrace();
        }
      }
       */
        }
    }
}
