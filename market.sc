// ********************************************************************************************
// *** Updates the supply and demand of different drugs in the market. This will help       ***
// *** determine drug prices and also who wants which drugs. The market can be slightly     ***
// *** different in different zones in each state, and more different in each city.         ***
// ********************************************************************************************

CONST
// Static
MARKET_INITIATED                = VAR_1
MARKET_DATA                     = VAR_2
MARKET_LAST_TIME_CHANGED        = VAR_3

// Struct Info
MARKET_DATA_SIZE                = 8

// Members
MARKET_DEMAND                   = -1@
MARKET_SUPPLY                   = -2@

// Functions
MarketData_GetPriceMultiplier   = -1@
END
VAR
THIS:array 4 of SUPERVAR
MARKET_DATA:array 4 of SUPERVAR
END

IF NOT market_initiated == 1
THEN
    READ_MEMORY 0xBA3794 4 OFF TEMP_1       // number of zones
    CALL @new 2 @MarketData TEMP_1 market_data
    ELSE_GOTO @Market_END
    market_initiated = 1
ELSE
    PRINT_FORMATTED "0x%X" 1 market_data
END

GET_GAME_TIMER TEMP_1
TEMP_1 -= 15000
IF 001D:   TEMP_1 > market_last_time_changed
THEN GET_GAME_TIMER market_last_time_changed
END
GOTO @Market_END

:MarketData // type()
RET 3 @MarketData_ctor 0 MARKET_DATA_SIZE

:MarketData_VTBL
HEX
@_object_vtbl
@MarketData_GetPriceMultiplier
END

:MarketData_ctor{\__(obj*)__}
0006: this[this] = @MarketData_VTBL
0007: this[MARKET_DEMAND] = 1.0
0007: this[MARKET_SUPPLY] = 1.0
RET 0

:MarketData_GetPriceMultiplier

:Market_END