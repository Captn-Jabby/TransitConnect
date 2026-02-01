import { getToken } from "./auth";

const BASE = `${process.env.REACT_APP_API_BASE_URL}/api/routes`;

const getAuthHeaders = () => {
  const token = getToken();
  return {
    "Content-Type": "application/json",
    "Authorization": `Bearer ${token}`
  };
};

/* ADD ROUTE */
export async function addRoute(payload) {
  const res = await fetch(`${BASE}/add`, {
    method: "POST",
    headers: getAuthHeaders(),
    body: JSON.stringify(payload),
  });

  if (!res.ok) throw new Error(await res.text());
  return res.json();
}

/* SEARCH ROUTE */
export async function searchRoutes(stop1, stop2, mode = "shortest") {
  const res = await fetch(
    `${BASE}/search?stop1=${encodeURIComponent(stop1)}&stop2=${encodeURIComponent(
      stop2
    )}&mode=${mode}`, {
    headers: getAuthHeaders()
  }
  );

  if (!res.ok) throw new Error(await res.text());
  return res.json();
}

/* GET ALL ROUTES */
export async function getAllRoutes() {
  const res = await fetch(`${BASE}/all`, {
    headers: getAuthHeaders()
  });
  if (!res.ok) throw new Error("Failed to load routes");
  return res.json();
}

/* ⭐ GET ALL STOP NAMES */
export async function fetchStops() {
  const res = await fetch(`${BASE}/stops`, {
    headers: getAuthHeaders()
  });
  if (!res.ok) throw new Error(await res.text());
  return res.json();
}
