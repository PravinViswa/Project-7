import React,{useEffect,useState} from 'react';
import {getUsers,loadUsers,searchUsers} from './services/api';
import Navbar from './components/Navbar';
import Footer from './components/Footer';
import SearchBar from './components/SearchBar';
import UserGrid from './components/UserGrid';

function App(){
  const [users, setUsers]=useState([]);
  const [error, setError]=useState('');
  const [loading, setLoading]=useState(false);

  const fetchUsers=async()=>{
    setLoading(true);setError('');
    try{
      const data=await getUsers();
      setUsers(Array.isArray(data)?data:[]);
    }catch{
      setError('Failed to fetch users');
    }finally{ setLoading(false);}
  };

  useEffect(()=>{ fetchUsers();},[]);

  const doSearch=async(q)=>{
    setLoading(true);setError('');
    try{
      const data=await searchUsers(q);
      setUsers(Array.isArray(data)?data:[]);
    }catch{
      setError('Search failed');
    }finally{setLoading(false);}
  };

  const doImport=async()=>{
    setLoading(true);setError('');
    try{
      await loadUsers();
      await fetchUsers();
    }catch{
      setError('Import failed');
    }finally{setLoading(false);}
  };

  return(
    <>
      <Navbar/>
      <div className="container">
        <div style={{ display:'flex',justifyContent:'space-between',alignItems:'center'}}>
          <h2>Users</h2>
          <button onClick={doImport} disabled={loading}>Import from External API</button>
        </div>

        <SearchBar onSearch={doSearch}/>

        {loading && <div>Loading...</div>}
        {error && <div style={{ color:'red',marginBottom:'1rem'}}>{error}</div>}

        <UserGrid users={users} />
      </div>
      <Footer />
    </>
  );
}

export default App;
