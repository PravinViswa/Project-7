const API_BASE =
  process.env.REACT_APP_API_BASE && process.env.REACT_APP_API_BASE.trim().length>0
    ? process.env.REACT_APP_API_BASE
    : 'http://localhost:8080';

async function getJson(url,options){
  const res=await fetch(url,options);
  if (!res.ok){
    const text=await res.text().catch(()=>'');
    throw new Error(`HTTP ${res.status} ${res.statusText} ${text}`);
  }
  return res.json();
}

export async function getUsers(){
  const body=await getJson(`${API_BASE}/api/users`);
  return body?.data??[];
}

export async function searchUsers(text){
  const body=await getJson(`${API_BASE}/api/users/search?text=${encodeURIComponent(text)}`);
  return body?.data ?? [];
}

export async function loadUsers(){
  const body=await getJson(`${API_BASE}/api/users/load`,{ method:'POST'});
  return body;
}
