
singleton TSShapeConstructor(ElephantEarDAE)
{
   baseShape = "./ElephantEar.DAE";
   loadLights = "0";
};

function ElephantEarDAE::onLoad(%this)
{
   %this.addImposter("64", "8", "0", "0", "512", "1", "0");
}
