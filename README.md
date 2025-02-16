# Virtual Threads vs WebFlux - Performance PoC

Este projeto compara **Spring Boot com Virtual Threads** vs **Spring Boot WebFlux** para medir desempenho em chamadas simultâneas a APIs externas.

## 🏗 Tecnologias Utilizadas
- **Spring Boot 3.2+**
- **Java 21 (Virtual Threads)**
- **Spring WebFlux (Bounded Elastic)**
- **Docker Compose**
- **Prometheus + Grafana**
- **Ferramentas de Benchmark (`hey`)**

## 🚀 Como Rodar a PoC

### 1️⃣ Clone o repositório
```bash
git clone https://github.com/seu-usuario/virtual-threads-vs-webflux-poc.git
cd virtual-threads-vs-webflux-poc
