
singleton TSShapeConstructor(CopalTree01DAE)
{
   baseShape = "./CopalTree01.DAE";
   loadLights = "0";
};

function CopalTree01DAE::onLoad(%this)
{
   %this.addImposter("0", "8", "0", "0", "512", "1", "0");
}
