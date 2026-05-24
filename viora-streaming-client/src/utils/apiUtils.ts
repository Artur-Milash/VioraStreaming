export const API_BASE = import.meta.env.VITE_API_URL ?? 'http://localhost:8080'

const tokenName = "JWT_TOKEN";
const tokenExpiryName = "JWT_TOKEN_EXPIRY";
const TOKEN_DURATION_MS = 60 * 60 * 1000;

export const getToken = (): string | null => {
  const token = localStorage.getItem(tokenName);
  if (!token) return null;

  const expiry = localStorage.getItem(tokenExpiryName);
  if (!expiry || Date.now() > Number(expiry)) {
    localStorage.removeItem(tokenName);
    localStorage.removeItem(tokenExpiryName);
    return null;
  }

  return token;
};

export const removeToken = () => {
  localStorage.removeItem(tokenName);
  localStorage.removeItem(tokenExpiryName);
};

export const saveTokenExpiry = () => {
  localStorage.setItem(tokenExpiryName, String(Date.now() + TOKEN_DURATION_MS));
};

export class ApiError extends Error {
  public status: number;

  constructor(status: number, message: string) {
    super(message);
    this.status = status;
  }
}

async function makeAuthenticatedRequest(url: string, options?: RequestInit) {
  const fullUrl = `${API_BASE}${url}`;
  const token = getToken();

  const existingHeaders = options?.headers instanceof Headers
      ? Object.fromEntries(options.headers.entries())
      : (options?.headers as Record<string, string> ?? {});

  const res = await fetch(fullUrl, {
    ...options,
    headers: {
      ...existingHeaders,
      ...(token ? {Authorization: `Bearer ${token}`} : {}),
    },
  });

  if (!res.ok) {
    const body = await res.json().catch(() => ({}));
    throw new ApiError(res.status, body.message ?? "Request failed");
  }

  return res;
}

export async function apiFetch(url: string, options?: RequestInit) {
  const res = await makeAuthenticatedRequest(url, options);
  return res.json();
}

export async function apiPostWithoutResult(url: string, options?: RequestInit) {
  await makeAuthenticatedRequest(url, options);
}

export function makeQueryFromProps<T extends Record<string, any>>(params: T): string {
  const query = new URLSearchParams();

  function flatten(obj: Record<string, any>, prefix = "") {
    Object.entries(obj).forEach(([key, value]) => {
      if (value === undefined || value === null) return;
      const fullKey = prefix ? `${prefix}.${key}` : key;
      if (typeof value === "object" && !Array.isArray(value)) {
        flatten(value, fullKey);
      } else {
        query.append(fullKey, String(value));
      }
    });
  }

  flatten(params);
  return query.toString();
}