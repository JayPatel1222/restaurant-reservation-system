```mermaid
classDiagram
    namespace Domain{
        
         class Menu{
            + Long id
            + String name
            + String description
            + LocalDate createdAt
            + List~MenuItem~ menuItems
        }

        class MenuItem{
            + String name
            + description
            + Menu menu
        }

        class Result~T~ {
            <<interface>>
            +getValue() T
            +getValidationErrors() Collection~ValidationError~
            +isEmpty() boolean
            +hasValue() boolean
            +hasValidationError() boolean
            +isSuccessful() boolean
            +isError() boolean
        }

        class ValidationError {
            + String field
            + String message
            + Object value
        }
    }

    namespace Application{

        class MenuService {
            <<interface>>
            + getAll() Result~Collection~Menu~~
            + getByMenuEvent(Long eventId)
            + get(Long id) Result~Menu~
            + create(Menu menu) Result~Menu~
            + update(Menu menu) Result~Menu~
            + delete(Long id) Result~void~
            + findByNameContainingIgnoreCase(String name) Result~Menu~
        }
        class MenuRepository {
            <<interface>>
            + getAll() List~Menu~
            + get(Long id) Menu
            + create(Menu menu) Menu
            + update(Menu menu) Menu
            + delete(Long id) void
            + exists(String name) boolean
        }
        class MenuItemRepository {
            <<interface>>
            + getAll(Long menuId) List~MenuItem~
            + get(String name) MenuItem
            + create(MenuItem menuItem) MenuItem
            + update(MenuItem menuItem) MenuItem
            + delete(Long id) void
            + exists(String name) boolean
        }

        class MenuItemService{
            <<interface>>
            + getAll() Result~Collection~MenuItem~~
            + getByMenu(Long menuId)
            + get(String name) Result~MenuItem~
            + create(MenuItem menuItem) Result~MenuItem~
            + update(MenuItem menuItem) Result~MenuItem~
            + delete(Long id) Result~void~
            + exists(String name) Result~boolean~
        }

        class MenuValidationService{
            + validate(Menu menu) Collection~ValidationError~
        }

        class MenuItemValidationService{
            + validate(MenuItem item) Collection~ValidationError~
        }
    }

    namespace Persistence {

        class MenuEntity {
            + Long id
            + String name
            + String description
            + LocalDate createdAt
            + List~MenuItemEntity~ menuItems
        
        }

        class MenuItemEntity{
            + String name
            + description
            + MenuEntity menu
        }
    }

    Menu --> "0..*" MenuItem : owns
    RestroEvent --> "0..1" Menu : optional

    MenuEntity --> "0..*" MenuItemEntity : owns
    Result~T~ <|-- ValidatedResult~T~ : extends

    MenuService ..> MenuValidationService : is Validated By
    MenuItemService ..> MenuItemValidationService : is Validated By

    MenuRepository ..> Menu : uses
    MenuItemRepository ..> MenuItem : uses
```
