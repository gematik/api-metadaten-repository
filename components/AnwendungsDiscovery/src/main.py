from fastapi import FastAPI
from fastapi.middleware.cors import CORSMiddleware
from controller import router as endpoints_router

app = FastAPI()

# Frontend auf localhost:8081 erlauben, um auf die API zugreifen zu könenn.
app.add_middleware(
    CORSMiddleware,
    allow_origins=["http://localhost:8081"],  
    allow_credentials=True,
    allow_methods=["GET", "POST", "PUT", "DELETE"],
    allow_headers=["*"],
)

app.include_router(endpoints_router)

if __name__ == '__main__':
    import uvicorn
    uvicorn.run(app, host="0.0.0.0", port=8081)
