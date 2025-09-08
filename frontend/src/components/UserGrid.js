import React,{useMemo,useState} from 'react';

export default function UserGrid({users=[]}){
  const [order,setOrder]=useState('asc');
  const [role,setRole]=useState('');

  const uniqueRoles=useMemo(
    ()=>Array.from(new Set(users.map(u=>u.role).filter(Boolean))),
    [users]
  );

  const filtered=useMemo(() =>{
    const byRole=users.filter(u=>!role||u.role===role);
    return byRole.sort((a,b)=>order==='asc'?a.age-b.age:b.age-a.age);
  }, [users,order,role]);

  return(
    <div>
      <div style={{marginBottom:'0.75rem',display:'flex',gap:'0.5rem',alignItems:'center'}}>
        <label>
          Sort by age:
          <select value={order} onChange={e=>setOrder(e.target.value)} style={{marginLeft:'0.5rem'}}>
            <option value="asc">Asc</option>
            <option value="desc">Desc</option>
          </select>
        </label>
        <label style={{marginLeft:'1rem'}}>
          Role:
          <select value={role} onChange={e=>setRole(e.target.value)} style={{marginLeft:'0.5rem'}}>
            <option value="">All</option>
            {uniqueRoles.map(r=><option key={r} value={r}>{r}</option>)}
          </select>
        </label>
      </div>

      <table>
        <thead>
          <tr>
            <th style={{width:80}}>ID</th>
            <th>First</th>
            <th>Last</th>
            <th>SSN</th>
            <th>Email</th>
            <th style={{width:80}}>Age</th>
            <th>Role</th>
          </tr>
        </thead>
        <tbody>
          {filtered.length===0?(
            <tr><td colSpan={7} style={{textAlign:'center',padding:'1rem'}}>No users found.</td></tr>
          ):(
            filtered.map(u=>(
              <tr key={u.id}>
                <td>{u.id}</td>
                <td>{u.firstName}</td>
                <td>{u.lastName}</td>
                <td>{u.ssn||'-'}</td>
                <td>{u.email}</td>
                <td>{u.age}</td>
                <td>{u.role||'-'}</td>
              </tr>
            ))
          )}
        </tbody>
      </table>
    </div>
  );
}
