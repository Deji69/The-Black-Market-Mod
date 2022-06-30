:GUI
HEX
0500 01 @InitProc
END
IF AND
    IS_PLAYER_PLAYING player1
    IS_PLAYER_SCRIPT_CONTROL_ON player1
THEN
    IF NOT IS_BIT_SET_L gSaveBools SBOOLS_bDrugSellTutDone
    THEN
        CALL @GetTotalDrugQuantity 0 TEMP_1
        IF TEMP_1 > 0
        THEN
            CALL @Async 2 @HelpDisplay HELPDISP_SELL_DRUGS
            SET_BIT_L gSaveBools SBOOLS_bDrugSellTutDone
        END
    END
    
    GET_CHAR_POINTER scplayer TEMP_1
    CALL_METHOD_RETURN 0x5408B0 0xB73458 1 0 TEMP_1 TEMP_2
    READ_MEMORY 0xBAA3FB 1 FALSE TEMP_3
    READ_MEMORY 0xB6F065 1 FALSE TEMP_4
    
    IF AND
        NOT TEMP_2 == 0
        TEMP_3 == 0
        TEMP_4 == 0
        VAR_4 == 0
        IS_CHAR_ON_FOOT scplayer
    THEN
        IF NOT VAR_1 == 1
        THEN
            IF IS_BUTTON_PRESSED PAD1 DPADRIGHT
            THEN
                VAR_3 = DPADRIGHT
            ELSE
                IF VAR_3 == DPADRIGHT
                THEN
                    VAR_1 = 1
                    VAR_2 = 0
                    VAR_3 = 0
                END
            END
        ELSE
            IF NOT IS_BIT_SET_L gSaveBools SBOOLS_sAbuseTutDone
            THEN
                CALL @GetTotalDrugQuantity 0 TEMP_1
                IF TEMP_1 > 0
                THEN
                    CALL @Async 2 @HelpDisplay HELPDISP_ABUSE_DRUGS
                    SET_BIT_L gSaveBools SBOOLS_sAbuseTutDone
                END
            END
            
            GET_VAR_POINTER gSaveDrugQtyBegin TEMP_4
            
            IF VAR_3 == DPADRIGHT
            THEN
                IF NOT IS_BUTTON_PRESSED PAD1 DPADRIGHT
                THEN VAR_3 = 0
                END
            END
            
            IF VAR_3 == DPADLEFT
            THEN
                IF NOT IS_BUTTON_PRESSED PAD1 DPADLEFT
                THEN VAR_3 = 0
                END
            END
            
            IF VAR_3 == 0
            THEN
                IF IS_BUTTON_PRESSED PAD1 DPADRIGHT
                THEN
                    VAR_2 += 1
                    VAR_3 = DPADRIGHT
                END
                IF IS_BUTTON_PRESSED PAD1 DPADLEFT
                THEN
                    VAR_2 -= 1
                    VAR_3 = DPADLEFT
                END
                IF VAR_2 > MAX_DRUG
                THEN VAR_2 = MIN_DRUG
                END
                IF VAR_2 < MIN_DRUG
                THEN VAR_2 = MAX_DRUG
                END
            END
            
            IF $Phone_Ringing_Flag == 0
            THEN
                IF IS_BUTTON_PRESSED PAD1 DPADUP
                THEN
                    IF VAR_3 == 0
                    THEN
                        IF 0019:   TEMP_4(VAR_2,4s) > 0
                        THEN
                            IF VAR_2 == WEED
                            THEN
                                000E: TEMP_4(VAR_2,4s) -= 1
                                CALL @Async 2 @GUI_TakeDrug VAR_2
                            ELSE
                                PRINT_HELP_FORMATTED "%s%s" "You do not have enough experience to experiment with this drug yet.~n~" "More drug use will be available in the next episode."
                            END
                        END
                        VAR_3 = DPADUP
                    END
                ELSE
                    IF VAR_3 == DPADUP
                    THEN
                        VAR_3 = 0
                    END
                END
            END
            
            CALL @DrawDrugRow 6 TEMP_4 240.0 350.0 48.0 VAR_2 140
        END
    ELSE
        IF VAR_1 == 1
        THEN
            VAR_1 = 0
            VAR_2 = 0
            VAR_3 = 0
            VAR_5 = 0
        END
    END
END
GOSUB @EndProc
RET 0

:GUI_TakeDrug       // (Async)
{
  SVar Indexes
    0   - Drug Index
    1   - Stage
    2   - Toke Counter
    3   - Last Toke Time
    4   - Cigar
    5   - Cigar Smoke
    6   - Cigar Stage
    7   - Control Stage
    8   - Help Displayed
}
IF 0039:   SVar[-1@] == 0
THEN
    IF NOT HAS_ANIMATION_LOADED "BLACKMARKET"
    THEN REQUEST_ANIMATION "BLACKMARKET"
    END
    IF 0039:   SVar[SVar] == WEED
    THEN
        REQUEST_MODEL 3044
    END
    CALL @IncDrugTakenStat 1 SVar[SVar]
    000A: SVar[-1@] += 1
END
IF 0039:   SVar[-1@] == 1
THEN
    IF HAS_ANIMATION_LOADED "BLACKMARKET"
    THEN
        IF 0039:   SVar[SVar] == WEED
        THEN
            IF HAS_MODEL_LOADED 3044
            THEN
                CREATE_OBJECT 3044 0 0 0 SVar[-4@]
                SET_OBJECT_VISIBLE SVar[-4@] FALSE
                
                IF IS_CHAR_ON_FOOT scplayer
                THEN
                    TASK_PLAY_ANIM_UPPER_BODY scplayer "SMOKE_IN" "BLACKMARKET" 8.0 0 0 0 0 -1
                    SET_PLAYER_ABLE_TO_CHOOSE_WEAPON player1 FALSE
                    CREATE_PARTICLE_ON_CHAR "EXHALE" scplayer 0.05 0.12 0 1 SVar[-5@]
                    ATTACH_PARTICLE_TO_CHAR SVar[-5@] scplayer 5
                ELSE
                END

                0006: SVar[-6@] = 0
                0006: SVar[-7@] = 0
                0006: SVar[-8@] = 0
                0006: SVar[-2@] = 0
                0006: SVar[-3@] = 0
                000A: SVar[-1@] += 1
            END
        ELSE
            TASK_PLAY_ANIM scplayer "DRUG_EAT" "BLACKMARKET" 4.0 0 0 0 0 -1
            000A: SVar[-1@] += 1
        END
    END
END
IF 0039:   SVar[-1@] == 2
THEN
    IF IS_PLAYER_PLAYING player1
    THEN
        IF 0039:   SVar[SVar] == WEED
        THEN
            IF 0039:   SVar[-8@] == 0
            THEN
                IF NOT IS_HELP_MESSAGE_BEING_DISPLAYED
                THEN
                    PRINT_HELP_FORMATTED "%s%s%s" "Use ~k~~PED_CYCLE_WEAPON_LEFT~ to take a short toke.~n~" "Use ~k~~PED_CYCLE_WEAPON_RIGHT~ to take a long toke.~n~" "Use ~k~~VEHICLE_ENTER_EXIT~ to throw."
                    0006: SVar[-8@] = 1
                END
            END
            
            IF NOT IS_CHAR_HOLDING_OBJECT scplayer SVar[-4@]
            THEN
                ATTACH_OBJECT_TO_PED scplayer SVar[-4@] 0.02 0.1 0.06 6 16 "NULL" "NULL" 1
                SET_CURRENT_CHAR_WEAPON scplayer WEAPONTYPE_UNARMED
            END
            
            GET_SCRIPT_TASK_STATUS scplayer 0x605 1@
            IF 1@ == 7
            THEN
                GET_SCRIPT_TASK_STATUS scplayer 0xA1A 1@
                IF 1@ == 7
                THEN
                    IF 0039:    SVar[-7@] == 0
                    THEN
                        IF AND
                            IS_PLAYER_CONTROL_ON player1
                            0039:   gDealGoingOn == 0
                        THEN
                            IF IS_BUTTON_PRESSED PAD1 LEFTSHOULDER2
                            THEN
                                TASK_PLAY_ANIM_UPPER_BODY scplayer "M_SMOKERH" "BLACKMARKET" 4.0 0 0 0 0 -1
                                0006: SVar[-7@] = LEFTSHOULDER2
                            ELSE
                                IF IS_BUTTON_PRESSED PAD1 RIGHTSHOULDER2
                                THEN
                                    TASK_PLAY_ANIM_UPPER_BODY scplayer "M_SmokeRHlong" "BLACKMARKET" 4.0 0 0 0 0 -1
                                    0006: SVar[-7@] = RIGHTSHOULDER2
                                END
                            END
                        END
                    END
                END
            END
            
            IF IS_CHAR_PLAYING_ANIM scplayer "SMOKE_IN"
            THEN
                GET_CHAR_ANIM_CURRENT_TIME scplayer "SMOKE_IN" 2@
                IF 2@ >= 0.2
                THEN
                    SET_OBJECT_VISIBLE SVar[-4@] TRUE
                ELSE
                    SET_OBJECT_VISIBLE SVar[-4@] FALSE
                END
                IF 2@ >= 0.45
                THEN
                    IF 0039:   SVar[-6@] == 0
                    THEN
                        START_PARTICLE SVar[-5@]
                        0006: SVar[-6@] = 1
                    END
                ELSE
                    IF 8039:   NOT SVar[-6@] == 0
                    THEN
                        STOP_PARTICLE SVar[-5@]
                        0006: SVar[-6@] = 0
                    END
                END
            ELSE
                IF 0039:   SVar[-6@] == 1
                THEN
                    STOP_PARTICLE SVar[-5@]
                    0006: SVar[-6@] = 0
                END
            END
            
            IF 0039:    SVar[-7@] == LEFTSHOULDER2
            THEN
                IF IS_CHAR_PLAYING_ANIM scplayer "M_SMOKERH"
                THEN
                    GET_CHAR_ANIM_CURRENT_TIME scplayer "M_SMOKERH" 2@
                    IF 2@ >= 0.45
                    THEN
                        IF 0039:   SVar[-6@] == 0
                        THEN
                            START_PARTICLE SVar[-5@]
                            0006: SVar[-6@] = 2
                            000A: SVar[-2@] += 1
                            CALL @IncDrugLevel 2 WEED 1
                            CALL @AddAmbientSmoke 1 0.6
                            GET_GAME_TIMER gWeedTimeLastTook
                        END
                    ELSE
                        IF 8039:   NOT SVar[-6@] == 0
                        THEN
                            STOP_PARTICLE SVar[-5@]
                            0006: SVar[-6@] = 0
                        END
                    END
                ELSE
                    IF 0039:   SVar[-6@] == 2
                    THEN
                        STOP_PARTICLE SVar[-5@]
                        0006: SVar[-6@] = 0
                    END
                    IF NOT IS_BUTTON_PRESSED PAD1 LEFTSHOULDER2
                    THEN
                        0006: SVar[-7@] = 0
                    END
                END
            END
            
            IF 0039:    SVar[-7@] == RIGHTSHOULDER2
            THEN
                IF IS_CHAR_PLAYING_ANIM scplayer "M_SMOKERHLONG"
                THEN
                    GET_CHAR_ANIM_CURRENT_TIME scplayer "M_SMOKERHLONG" 2@
                    IF 2@ >= 0.5
                    THEN
                        IF 0039:   SVar[-6@] == 0
                        THEN
                            START_PARTICLE SVar[-5@]
                            0006: SVar[-6@] = 3
                            000A: SVar[-2@] += 2
                            CALL @IncDrugLevel 2 WEED 2
                            CALL @AddAmbientSmoke 1 1.0
                        END
                    ELSE
                        IF 8039:   NOT SVar[-6@] == 0
                        THEN
                            STOP_PARTICLE SVar[-5@]
                            0006: SVar[-6@] = 0
                        END
                    END
                ELSE
                    IF 0039:   SVar[-6@] == 3
                    THEN
                        STOP_PARTICLE SVar[-5@]
                        0006: SVar[-6@] = 0
                    END
                    IF NOT IS_BUTTON_PRESSED PAD1 RIGHTSHOULDER2
                    THEN
                        0006: SVar[-7@] = 0
                    END
                END
            END
    
            IF 0019:   SVar[-2@] > 8
            THEN 0006: SVar[-1@] = -1
            END
            
            IF IS_CHAR_PLAYING_ANIM scplayer "EAT_VOMIT_P"
            THEN 0006: SVar[-1@] = -1
            END
            
            IF IS_BUTTON_PRESSED PAD1 TRIANGLE
            THEN
                IF IS_CHAR_PLAYING_ANIM scplayer "M_SMOKERHLONG"
                THEN
                    SET_CHAR_ANIM_CURRENT_TIME scplayer "M_SMOKERHLONG" TO 1.0
                END
                IF IS_CHAR_PLAYING_ANIM scplayer "M_SMOKERH"
                THEN
                    SET_CHAR_ANIM_CURRENT_TIME scplayer "M_SMOKERH" TO 1.0
                END
                0006: SVar[-1@] = -1
            END
        ELSE
            HIDE_CHAR_WEAPON_FOR_SCRIPTED_CUTSCENE scplayer OFF
            0006: 1@ = -1
        END
    ELSE
        HIDE_CHAR_WEAPON_FOR_SCRIPTED_CUTSCENE scplayer OFF
        0006: 1@ = -1
    END
END
IF 0039:   SVar[-1@] == -1
THEN
    IF 0039:   SVar[SVar] == WEED
    THEN
        DELETE_PARTICLE SVar[-5@]
        MARK_MODEL_AS_NO_LONGER_NEEDED 3044
        IF IS_PLAYER_PLAYING player1
        THEN
            DROP_OBJECT scplayer 1
        ELSE
            DELETE_OBJECT SVar[-4@]
        END
        SET_PLAYER_ABLE_TO_CHOOSE_WEAPON player1 TRUE
    END
    CALL @Stat_IncDrugExp 1 SVar[-2@]
    IF 8019:   NOT gNumAnimReferences > 0
    THEN
        REMOVE_ANIMATION "BLACKMARKET"
    END
    RET 1 TRUE
END
RET 1 FALSE