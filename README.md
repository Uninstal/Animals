# Animals [being developed...]

### Work
- [x] Base
- [x] config.yml
- [x] animals.yml
- [ ] Manager
- [ ] Handler
- [ ] Commands
- [x] Taming feeding
- [ ] Taming damaging
- [x] Taming dragon

### Description

The plugin includes RPG pets that can be tamed using different methods.  
There are 3 types in total so far:
1. By feeding.
2. With the help of the murder.
3. Using an egg. (Only for the dragon)

### config.yml

All settings have a description, so I think you can figure it out for yourself. :)
```yaml
#Настройки
settings:
  
  db:
    
    #Хост
    host: '127.0.0.1'
    #Пользователь
    user: 'root'
    #Пароль
    pass: 'password'
    #База
    base: 'base'
  
  #Лимиты
  #<group>: <limit>
  limits:
    
    #Дефолтное значение
    #Используется, если у игрока
    #группа, которой нет в списке
    default: 2
    
    #Другие группы
    vip: 3
    prem: 4
    admin: 10
    
#Сообщения
messages:
  
  #Информация по командам
  info: |-
    &r &r
    &r &c/mobs list &7- список прирученных животных.
    &r &c/mobs give <nick> &7- передать питомца игроку.
    &r &r
    &r &c/dragon info &7- информация о драконе.
    &r &c/dragon kill &7- убить дракона.
    &r &c/dragon trage <nick> &7- обменять дракона.
    &r &c/dragon tp &7- телепортация к дракону.
    &r &c/dragon get &7- переместить дракона.
    &r &r
  #Информация о драконе
  dragon-info: |-
    &r &r
    &r &aМесторасположение: &e<x> <y> <z>
    &r &aЗдоровье: <hp>
    &r &aУровень: <level>
    &r &aСпособности:
    &r &e<abilities>
    &r &r
  #Игрок достиг лимита питомцев
  limit: '&cВы достигли лимита питомцев.'
  #Задержка на кормление
  cooldown: '&cВы сможете снова покормить через &6<time> &cсекунд.'
  #Игрок передал питомца другому игроку
  give: '&aВы передали своего питомца.'
  #Игрок приручил животное
  tame: '&aВы приручили животное.'
```
### animals.yml
```yaml
#Тип животного
#sheep - овца
sheep:
  
  #Предмет, которым приручается
  #wheat - пшеница (не блок)
  #Для стандартного типа приручения
  #Можно убрать, если используется другой
  item: wheat
  
  #Количество требуемой еды
  #Еда настраивается в item
  amount: 10
  
  #Задержка кормления
  #В секундах
  cooldown: 60
  
  #Тип приручения
  #default - стандартный, через предмет
  #crackshot - через оружия из CrackShot
  #dragon - для эндер дракона
  type: default
  
#Свинья
#НЕ РАБОТАЕТ
pig:
  
  #Оружие, которым приручается
  #ak-47 - автомат
  #Для приручения через CrackShot
  #Можно убрать, если используется другой
  weapon: ak-47
  
  #Необходимый урон
  damage: 10
  
  #Тип приручения
  #default - стандартный, через предмет
  #crackshot - через оружия из CrackShot
  #dragon - для эндер дракона
  type: crackshot
  
#Дракон
dragon:
  
  #Предмет, который нужно
  #вложить в яйцо
  #item: <type>
  item: nether_star
  
  #Количество требуемой еды
  #Еда настраивается в item
  amount: 10
  
  #Задержка
  #В секундах
  cooldown: 60
  
  #Тип приручения
  #default - стандартный, через предмет
  #crackshot - через оружия из CrackShot
  #dragon - для эндер дракона
  type: dragon
```
### API
To work with the plugin, it is enough to interact with one class - `AnimalsManager`.   
It has all the necessary static methods to work directly.

|Method|Description|
|:-:|:-:|
|`isTame(UUID animal)`|outputs a `boolean` that tells you if the pet is tamed.|
|`isDragon(UUID animal)`|outputs a `boolean` that tells you whether the tamed pet is a dragon.|
|`getNonTamedAnimal(UUID animal)`|outputs `AnimalNonTamed`, which is the object of the TAMING animal.|
|`getAnimal(UUID animal)`|outputs `AnimalTamedDefault`, which is the object of a default animal.|
|`getDragon(UUID animal)`|outputs `AnimalTamedDragon`, which is the object of a dragon.|
