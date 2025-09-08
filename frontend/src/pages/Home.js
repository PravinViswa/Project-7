import React,{useEffect,useState} from 'react';
import axios from 'axios';

const Home=()=>{
  const [users,setUsers]=useState([]);
  const [loading,setLoading]=useState(true);
  const [error,setError]useState('');

  useEffect(()=>{
    axios.get('http://localhost:8080/api/users')
      .then(response=>{
        setUsers(response.data?.data??[]);
        setLoading(false);
      })
      .catch(err=>{
        setError('Failed to fetch users');
        setLoading(false);
      });
  },[]);

  if (loading) return <p>Loading users...</p>;
  if (error) return <p>{error}</p>;

  return(
    <div style={{ padding:'2rem'}}>
      <h1>User List</h1>
      {users.length===0?(
        <p>No users found.</p>
      ):(
        <table border="1" cellPadding="8">
          <thead>
            <tr>
              <th>ID</th>
              <th>First</th>
              <th>Last</th>
              <th>Email</th>
              <th>Age</th>
              <th>Role</th>
            </tr>
          </thead>
          <tbody>
            {users.map(u=>(
              <tr key={u.id}>
                <td>{u.id}</td>
                <td>{u.firstName}</td>
                <td>{u.lastName}</td>
                <td>{u.email}</td>
                <td>{u.age}</td>
                <td>{u.role||'-'}</td>
              </tr>
            ))}
          </tbody>
        </table>
      )}
    </div>
  );
};

export default Home;
