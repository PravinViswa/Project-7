import React,{useState} from 'react';

export default function SearchBar({onSearch}){
  const [q,setQ]=useState('');

  const handle=()=>{
    if(q.trim().length>=3) onSearch(q.trim());
  };

  const handleKeyDown=(e)=>{
    if(e.key==='Enter') handle();
  };

  return(
    <div style={{ marginBottom:'1rem',display:'flex',alignItems:'center'}}>
      <input
        value={q}
        onChange={(e)=>setQ(e.target.value)}
        onKeyDown={handleKeyDown}
        placeholder="Search by first/last name or SSN (min 3 chars)"
        style={{ flex:1,maxWidth:600}}
      />
      <button onClick={handle} disabled={q.trim().length<3}>Search</button>
    </div>
  );
}
