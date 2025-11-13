# OOP CONCEPTS - VISUAL CLASS DIAGRAM

```
                    ┌─────────────────────────────┐
                    │      <<abstract>>           │
                    │        Person               │
                    │  (INHERITANCE BASE CLASS)   │
                    ├─────────────────────────────┤
                    │ # id: int                   │
                    │ # name: String              │
                    │ # country: String           │
                    ├─────────────────────────────┤
                    │ + getId(): int              │
                    │ + getName(): String         │
                    │ + getCountry(): String      │
                    │ + getRole(): String         │ ← Abstract method
                    │ + displayInfo(): void       │ ← Can be overridden
                    └──────────────┬──────────────┘
                                   │
                                   │
                      ┌────────────▼──────────────┐
                      │        Player              │
                      │  (INHERITANCE - CHILD)     │
                      ├────────────────────────────┤
                      │ - role: PlayerRole         │
                      │ - basePrice: double        │
                      │ - status: PlayerStatus     │
                      │ - marketValue: double      │
                      ├────────────────────────────┤
                      │ + getRole(): String        │ ← Override
                      │ + getRole(boolean): Enum   │ ← Overload (POLYMORPHISM)
                      │ + displayInfo(): void      │ ← Override (POLYMORPHISM)
                      │ + getMarketValue(): double │ ← From Valuable interface
                      │ + updateValue(double): void│ ← From Valuable interface
                      │ + getAuctionStatus(): Stat │ ← From Auctionable interface
                      │ + setAuctionStatus(Stat)   │ ← From Auctionable interface
                      └────────────┬───────────────┘
                                   │
                                   │ implements (MULTIPLE INHERITANCE via interfaces)
                                   │
                      ┌────────────▼──────────────┐   ┌─────────────────────────┐
                      │   <<interface>>            │   │   <<interface>>         │
                      │     Valuable               │   │    Auctionable          │
                      ├────────────────────────────┤   ├─────────────────────────┤
                      │ + getMarketValue(): double │   │ + getAuctionStatus()    │
                      │ + updateValue(double)      │   │ + setAuctionStatus()    │
                      └────────────────────────────┘   └─────────────────────────┘
```

## CONCEPT MAPPING

### 1. INHERITANCE ⬆️ 
- Player → Person (Player IS-A Person)
- Arrow shows "extends" relationship

### 2. MULTIPLE INHERITANCE ↔️
- Player → Valuable interface
- Player → Auctionable interface
- Player gets behavior from multiple sources

### 3. POLYMORPHISM 🔄

**Method Overriding:**
```
Person.getRole()     ────►  Player.getRole()    (Different implementation)

Person.displayInfo() ────►  Player.displayInfo() (Different implementation)
```

**Method Overloading:**
```
Player.getRole()         ────► Returns String
Player.getRole(boolean)  ────► Returns PlayerRole enum
(Same name, different parameters)
```

**Runtime Polymorphism:**
```java
Person p1 = new Player(...);    // OK - Player IS-A Person
Person p2 = new Player(...);    // OK - Player IS-A Person

p1.displayInfo();  // Calls Player's version
p2.displayInfo();  // Calls Player's version
// Same method call, different behavior based on actual object type
```

### 4. ENCAPSULATION 🔒
```
Player class:
├── private role           ─► Cannot access from outside
├── private basePrice      ─► Cannot access from outside
├── private status         ─► Cannot access from outside
│
├── public getRole()       ─► Controlled READ access
├── public getBasePrice()  ─► Controlled READ access
└── public setStatus()     ─► Controlled WRITE access (with validation)
```

---

## RELATIONSHIPS SUMMARY

```
Person (1 parent)
    │
    └── Player (child) ── implements ──► Valuable (interface 1)
                                      │
                                      └─► Auctionable (interface 2)
```

**Legend:**
- Solid arrow (───►): Inheritance (extends)
- Dashed arrow (- - ►): Implementation (implements)
- # : protected
- - : private
- + : public

---

## KEY POINTS FOR PROFESSOR

1. **Single Inheritance Tree**: Person → Player (Java allows only single class inheritance)
2. **Multiple Interface Implementation**: Player implements 2 interfaces (Java's way of multiple inheritance)
3. **Polymorphism**: Same method names, different behaviors (Override + Overload)
4. **Encapsulation**: All data fields private, accessed through public methods

This design is:
- ✅ Simple and clear
- ✅ Follows OOP principles
- ✅ Integrated into real project code
- ✅ Easy to explain and demonstrate
